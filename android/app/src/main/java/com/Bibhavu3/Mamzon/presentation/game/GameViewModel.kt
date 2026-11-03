package com.Bibhavu3.Mamzon.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.data.sample.SampleData
import com.Bibhavu3.Mamzon.domain.model.Outcome
import com.Bibhavu3.Mamzon.domain.model.PlantSpecies
import com.Bibhavu3.Mamzon.domain.model.RoundResult
import com.Bibhavu3.Mamzon.domain.repository.GardenRepository
import com.Bibhavu3.Mamzon.domain.usecase.AwardSeedsUseCase
import com.Bibhavu3.Mamzon.domain.usecase.ComputeFlowUseCase
import com.Bibhavu3.Mamzon.domain.usecase.EvaluateRoundUseCase
import com.Bibhavu3.Mamzon.domain.usecase.FindHintUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    levelId: Int,
    private val repository: GardenRepository,
    private val computeFlow: ComputeFlowUseCase,
    private val evaluateRound: EvaluateRoundUseCase,
    private val findHint: FindHintUseCase,
    private val awardSeeds: AwardSeedsUseCase
) : ViewModel() {

    private val level = SampleData.level(levelId)

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RoundResult>(Channel.BUFFERED)
    val events: Flow<RoundResult> = _events.receiveAsFlow()

    private var idleJob: Job? = null
    private var settleJob: Job? = null
    private var hintJob: Job? = null
    private var resolved = false

    init {
        viewModelScope.launch {
            repository.progress.collect { progress ->
                _state.update {
                    it.copy(seeds = progress.totalSeeds, hintsEnabled = progress.hintsEnabled)
                }
            }
        }
        startRound()
    }

    fun onTileTap(index: Int) {
        val current = _state.value
        if (!current.interactive) return
        val tile = current.tiles.getOrNull(index) ?: return
        if (!tile.rotatable) return
        val tiles = current.tiles.toMutableList()
        tiles[index] = tile.rotated()
        val flow = computeFlow(tiles, GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS)
        val report = evaluateRound(tiles, flow.wet, GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS)
        val movesLeft = current.movesLeft - 1
        val roundOver = report.solved || movesLeft <= 0
        _state.value = current.copy(
            tiles = tiles,
            wet = flow.wet,
            distance = flow.distance,
            hydrated = report.hydrated,
            drowned = report.drowned,
            required = report.required,
            movesLeft = movesLeft,
            hintIndex = -1,
            phase = if (roundOver) GamePhase.SETTLING else GamePhase.PLAYING
        )
        when {
            report.solved -> settle(Outcome.RESTORED)
            movesLeft <= 0 -> settle(Outcome.OUT_OF_MOVES)
            else -> armIdle()
        }
    }

    fun requestHint() {
        val current = _state.value
        if (!current.interactive) return
        if (current.seeds < GameConfig.HINT_COST) return
        val index = findHint(current.tiles, level.solvedTiles)
        if (index < 0) return
        repository.update { it.copy(totalSeeds = it.totalSeeds - GameConfig.HINT_COST) }
        _state.update { it.copy(hintIndex = index) }
        hintJob?.cancel()
        hintJob = viewModelScope.launch {
            delay(GameConfig.HINT_HIGHLIGHT_MS)
            _state.update { it.copy(hintIndex = -1) }
        }
        armIdle()
    }

    fun resetBoard() {
        if (_state.value.phase == GamePhase.FINISHED) return
        startRound()
    }

    fun pause() {
        idleJob?.cancel()
        hintJob?.cancel()
        _state.update { it.copy(paused = true, hintIndex = -1) }
    }

    fun resume() {
        _state.update { it.copy(paused = false) }
        if (_state.value.phase == GamePhase.PLAYING) armIdle()
    }

    override fun onCleared() {
        idleJob?.cancel()
        settleJob?.cancel()
        hintJob?.cancel()
        super.onCleared()
    }

    private fun startRound() {
        resolved = false
        idleJob?.cancel()
        settleJob?.cancel()
        hintJob?.cancel()
        val tiles = level.startTiles
        val flow = computeFlow(tiles, GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS)
        val report = evaluateRound(tiles, flow.wet, GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS)
        val progress = repository.progress.value
        _state.value = GameUiState(
            levelId = level.id,
            zone = level.zone,
            tiles = tiles,
            wet = flow.wet,
            distance = flow.distance,
            hydrated = report.hydrated,
            drowned = report.drowned,
            required = report.required,
            movesLeft = level.movesAllowed,
            movesAllowed = level.movesAllowed,
            seeds = progress.totalSeeds,
            hintsEnabled = progress.hintsEnabled,
            phase = GamePhase.LOADING
        )
        viewModelScope.launch {
            delay(GameConfig.BOARD_PREPARE_MS)
            _state.update { it.copy(phase = GamePhase.PLAYING) }
            armIdle()
        }
    }

    private fun armIdle() {
        idleJob?.cancel()
        idleJob = viewModelScope.launch {
            delay(GameConfig.IDLE_BACKSTOP_MS)
            if (_state.value.phase == GamePhase.PLAYING) {
                resolve(Outcome.WITHERED)
            }
        }
    }

    private fun settle(outcome: Outcome) {
        idleJob?.cancel()
        hintJob?.cancel()
        settleJob?.cancel()
        settleJob = viewModelScope.launch {
            delay(GameConfig.ROUND_SETTLE_MS)
            resolve(outcome)
        }
    }

    private fun resolve(outcome: Outcome) {
        if (resolved) return
        resolved = true
        idleJob?.cancel()
        val current = _state.value
        val restored = outcome == Outcome.RESTORED
        val hydratedCount = current.hydrated.size
        val spareMoves = if (restored) current.movesLeft else 0
        val earned = awardSeeds(hydratedCount, spareMoves, restored)
        val unlockedSpecies = buildSet {
            current.tiles.forEach { tile ->
                if (tile.index in current.hydrated) {
                    tile.species?.let { add(it.id) }
                }
            }
            if (restored) add(PlantSpecies.BLUE_MORPHO.id)
            if (earned > 0) add(PlantSpecies.SEED_POUCH.id)
        }
        var beatRecord = false
        repository.update { progress ->
            beatRecord = earned > progress.bestSeedsInRound
            progress.copy(
                totalSeeds = progress.totalSeeds + earned,
                bestSeedsInRound = maxOf(progress.bestSeedsInRound, earned),
                levelsCleared = if (restored) progress.levelsCleared + 1 else progress.levelsCleared,
                highestLevelUnlocked = if (restored) {
                    maxOf(progress.highestLevelUnlocked, SampleData.nextLevelId(current.levelId))
                } else {
                    progress.highestLevelUnlocked
                },
                discoveredSpecies = progress.discoveredSpecies + unlockedSpecies
            )
        }
        val result = RoundResult(
            levelId = current.levelId,
            nextLevelId = if (restored) SampleData.nextLevelId(current.levelId) else current.levelId,
            zone = current.zone,
            outcome = outcome,
            hydrated = hydratedCount,
            required = current.required,
            movesUsed = current.movesUsed,
            movesAllowed = current.movesAllowed,
            seedsEarned = earned,
            newBest = beatRecord && earned > 0
        )
        _state.update { it.copy(phase = GamePhase.FINISHED) }
        viewModelScope.launch {
            _events.send(result)
        }
    }
}

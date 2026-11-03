package com.Bibhavu3.Mamzon.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bibhavu3.Mamzon.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameOverUiState(
    val totalSeeds: Int = 0,
    val levelsCleared: Int = 0,
    val bestSeedsInRound: Int = 0
)

class GameOverViewModel(
    repository: GardenRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.progress.collect { progress ->
                _state.value = GameOverUiState(
                    totalSeeds = progress.totalSeeds,
                    levelsCleared = progress.levelsCleared,
                    bestSeedsInRound = progress.bestSeedsInRound
                )
            }
        }
    }
}

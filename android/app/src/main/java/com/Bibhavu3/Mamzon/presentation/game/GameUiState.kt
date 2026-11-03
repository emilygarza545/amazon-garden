package com.Bibhavu3.Mamzon.presentation.game

import com.Bibhavu3.Mamzon.domain.model.Tile
import com.Bibhavu3.Mamzon.domain.model.ZoneId

enum class GamePhase {
    LOADING,
    PLAYING,
    SETTLING,
    FINISHED
}

data class GameUiState(
    val levelId: Int = 1,
    val zone: ZoneId = ZoneId.MIST_BASIN,
    val tiles: List<Tile> = emptyList(),
    val wet: Set<Int> = emptySet(),
    val distance: Map<Int, Int> = emptyMap(),
    val hydrated: Set<Int> = emptySet(),
    val drowned: Set<Int> = emptySet(),
    val required: Int = 0,
    val movesLeft: Int = 0,
    val movesAllowed: Int = 0,
    val seeds: Int = 0,
    val hintIndex: Int = -1,
    val hintsEnabled: Boolean = true,
    val phase: GamePhase = GamePhase.LOADING,
    val paused: Boolean = false
) {
    val interactive: Boolean = phase == GamePhase.PLAYING && !paused

    val movesUsed: Int = movesAllowed - movesLeft
}

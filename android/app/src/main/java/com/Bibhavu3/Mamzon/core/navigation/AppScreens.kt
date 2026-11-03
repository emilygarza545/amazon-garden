package com.Bibhavu3.Mamzon.core.navigation

import com.Bibhavu3.Mamzon.domain.model.RoundResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val levelId: Int, val attempt: Int) : Screen

    data class GameOver(val result: RoundResult) : Screen
}

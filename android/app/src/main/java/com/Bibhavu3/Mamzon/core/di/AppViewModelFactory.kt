package com.Bibhavu3.Mamzon.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Bibhavu3.Mamzon.presentation.app.AppViewModel
import com.Bibhavu3.Mamzon.presentation.game.GameViewModel
import com.Bibhavu3.Mamzon.presentation.gameover.GameOverViewModel
import com.Bibhavu3.Mamzon.presentation.menu.MenuViewModel
import com.Bibhavu3.Mamzon.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val levelId: Int = 1
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.gardenRepository)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                levelId = levelId,
                repository = ServiceLocator.gardenRepository,
                computeFlow = ServiceLocator.computeFlow,
                evaluateRound = ServiceLocator.evaluateRound,
                findHint = ServiceLocator.findHint,
                awardSeeds = ServiceLocator.awardSeeds
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(ServiceLocator.gardenRepository)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}

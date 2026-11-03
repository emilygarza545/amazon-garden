package com.Bibhavu3.Mamzon.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bibhavu3.Mamzon.core.di.AppViewModelFactory
import com.Bibhavu3.Mamzon.core.navigation.Screen
import com.Bibhavu3.Mamzon.presentation.game.GameScreen
import com.Bibhavu3.Mamzon.presentation.gameover.GameOverScreen
import com.Bibhavu3.Mamzon.presentation.menu.MenuScreen
import com.Bibhavu3.Mamzon.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = screen !is Screen.Splash) {
        if (!appViewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when (targetState) {
                    is Screen.GameOver ->
                        (fadeIn(tween(360)) + scaleIn(initialScale = 0.94f)) togetherWith
                            fadeOut(tween(200))
                    is Screen.Game ->
                        (slideInHorizontally(tween(300)) { full -> full / 6 } +
                            fadeIn(tween(300))) togetherWith fadeOut(tween(200))
                    else -> fadeIn(tween(420)) togetherWith fadeOut(tween(260))
                }
            },
            label = "appRootTransition"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(onFinished = appViewModel::showMenu)
                is Screen.Menu -> MenuScreen(onPlay = appViewModel::openGame)
                is Screen.Game -> GameScreen(
                    levelId = current.levelId,
                    attempt = current.attempt,
                    onFinished = appViewModel::showResult,
                    onExit = appViewModel::showMenu
                )
                is Screen.GameOver -> GameOverScreen(
                    result = current.result,
                    onPlayAgain = appViewModel::openGame,
                    onMenu = appViewModel::showMenu
                )
            }
        }
    }
}

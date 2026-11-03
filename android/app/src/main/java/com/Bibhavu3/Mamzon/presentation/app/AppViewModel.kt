package com.Bibhavu3.Mamzon.presentation.app

import androidx.lifecycle.ViewModel
import com.Bibhavu3.Mamzon.core.navigation.Screen
import com.Bibhavu3.Mamzon.domain.model.RoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = ArrayDeque<Screen>()

    private var roundCounter = 0

    fun showMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun openGame(levelId: Int) {
        val current = _screen.value
        if (current !is Screen.Splash && current !is Screen.GameOver) {
            backStack.addLast(current)
        }
        roundCounter += 1
        _screen.value = Screen.Game(levelId, roundCounter)
    }

    fun showResult(result: RoundResult) {
        _screen.value = Screen.GameOver(result)
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }
}

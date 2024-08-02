package io.trading.bot.ui.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.annotation.Single


@Single
class Router {

    private val _navigationFlow = MutableSharedFlow<Screen>()
    val navigationFlow = _navigationFlow.asSharedFlow()

    suspend fun navigateTo(screen: Screen) {
        _navigationFlow.emit(screen)
    }
}
package io.trading.bot.ui.common

import androidx.navigation.NavGraphBuilder


interface ScreenProvider {

    val builder: NavGraphBuilder.() -> Unit
}

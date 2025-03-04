package io.trading.bot.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.trading.bot.service.IntentLauncher
import io.trading.bot.ui.common.Router
import io.trading.bot.ui.common.Screen
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val router: Router,
    private val intentLauncher: IntentLauncher,
) : ViewModel() {

    fun openNotificationService() = intentLauncher.openNotificationSettings()

    fun navigateTo(screen: Screen) {
        viewModelScope.launch { router.navigateTo(screen) }
    }
}
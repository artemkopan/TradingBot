package io.trading.bot.ui.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.trading.bot.model.Notification
import io.trading.bot.repo.rh.RobinHoodRepo
import io.trading.bot.usecase.notification.HandleNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DebugViewModel(
    private val handleNotificationUseCase: HandleNotificationUseCase,
    private val robinHoodRepo: RobinHoodRepo
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun getQuotes() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
            robinHoodRepo.getQuote("TSLA")
                .onSuccess {
                    _state.value = _state.value.copy(
                        loading = false,
                        status = "Stock: ${it.stock}, Last Price: ${it.lastPrice}"
                    )
                }
                .updateStateOnFailure()
        }
    }

    fun sendRaiseNotification() {
        handleNotificationUseCase.invoke(
            Notification(
                packageName = "com.robinhood.android",
                title = "NVDA RSI(14) rose above upper level",
                text = "🔔 RSI(14) for NVDA crossed your custom upper level of 50.00.",
            )
        )
    }

    fun sendBelowNotification() {
        handleNotificationUseCase.invoke(
            Notification(
                packageName = "com.robinhood.android",
                title = "NVDA RSI(14) fell below lower level",
                text = "\uD83D\uDD14 RSI(14) for NVDA crossed your custom lower level of 40.00.",
            )
        )
    }

    fun sendUnhandledNotification() {
        handleNotificationUseCase.invoke(
            Notification(
                packageName = "com.robinhood.android",
                title = "Something happened",
                text = "Ooops",
            )
        )

    }

    private fun Result<*>.updateStateOnFailure() {
        onFailure {
            _state.value = _state.value.copy(
                loading = false,
                status = "Error: ${it.message}"
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val status: String = ""
    )
}
package io.trading.bot.usecase.notification

import io.trading.bot.model.Notification
import io.trading.bot.worker.LogMessageWorkerScheduler
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class HandleNotificationUseCase(private val logMessageWorkerScheduler: LogMessageWorkerScheduler) {

    fun invoke(notification: Notification) {
        if (notification.packageName != RH_PACKAGE_NAME) {
            Timber.i("Notification is not from Robinhood, skip it")
            return
        }
        val matchResult = regex.find(notification.text)

        if (matchResult == null) {
            Timber.e("Notification text doesn't match the regex.\nNotification: $notification")
            return
        }

        val (stockSymbol, indicator, level) = matchResult.destructured
        val buyOrSell = if (indicator == "lower") "buy" else "sell"

        logMessageWorkerScheduler.schedule(
            """
            "Stock: $stockSymbol"
            "Level: $level"
            "Indicator: $buyOrSell"
        """.trimIndent()
        )

        // todo make api call to Robinhood
    }

    private companion object {
        const val RH_PACKAGE_NAME = "com.robinhood.android"
        val regex =
            """RSI\(\d+\)\s*for\s*(\w+)\s*crossed\s*your\s*custom\s*(lower|upper)\s*level\s*of\s*(\d+\.\d{2})""".toRegex()
    }
}
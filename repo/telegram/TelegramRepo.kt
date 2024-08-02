package io.trading.bot.repo.telegram

import io.ktor.client.statement.HttpResponse
import io.trading.bot.network.telegram.TelegramNetwork
import org.koin.core.annotation.Factory

@Factory
class TelegramRepo(private val telegramNetwork: TelegramNetwork) {

    suspend fun sendMessage(message: String): Result<HttpResponse> {
        return telegramNetwork.sendMessage(message)
    }
}
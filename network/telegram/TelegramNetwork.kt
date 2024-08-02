package io.trading.bot.network.telegram

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.trading.bot.network.telegram.request.TelegramMessage
import io.trading.bot.service.SecretsProvider
import org.koin.core.annotation.Factory

@Factory
class TelegramNetwork(
    private val httpClient: HttpClient,
    private val secretsProvider: SecretsProvider
) {

    suspend fun sendMessage(message: String) = runCatching {
        httpClient.post(
            "https://api.telegram.org/${secretsProvider.telegramBotId()}/sendMessage"
        ) {
            setBody(TelegramMessage(secretsProvider.telegramChatId(), message))
        }
    }
}
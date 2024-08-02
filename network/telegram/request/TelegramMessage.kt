package io.trading.bot.network.telegram.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramMessage(@SerialName("chat_id") val chatId: String, val text: String)

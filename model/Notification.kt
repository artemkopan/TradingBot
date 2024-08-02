package io.trading.bot.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val packageName: String,
    val title: String,
    val text: String,
)
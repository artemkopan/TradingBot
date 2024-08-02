package io.trading.bot.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    val token: String
)
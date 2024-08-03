package io.trading.bot.network.robinhood.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeRequest(
    @SerialName("response") val response: String? = null
)


package io.trading.bot.network.robinhood.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val challenge: Challenge? = null,
    val detail: String? = null
) {

    @Serializable
    data class Challenge(
        val mfaStatus: String? = null,
        val expiresAt: String? = null,
        val remainingAttempts: Int? = null,
        val updatedAt: String? = null,
        val flowId: String? = null,
        val remainingRetries: Int? = null,
        val alternateType: String? = null,
        val id: String? = null,
        val type: String? = null,
        val user: String? = null,
        val status: String? = null
    )
}
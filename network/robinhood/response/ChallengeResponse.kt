package io.trading.bot.network.robinhood.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeResponse(
    @SerialName("mfa_status")
    val mfaStatus: String? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("remaining_attempts")
    val remainingAttempts: Int? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("flow_id")
    val flowId: String? = null,
    @SerialName("remaining_retries")
    val remainingRetries: Int? = null,
    @SerialName("alternate_type")
    val alternateType: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("user")
    val user: String? = null,
    @SerialName("status")
    val status: String? = null
)


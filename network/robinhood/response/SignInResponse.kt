package io.trading.bot.network.robinhood.response

import io.trading.bot.model.SignInResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInChallengeResponse(
    @SerialName("challenge") val challenge: Challenge? = null,
    @SerialName("detail") val detail: String? = null
) {

    @Serializable
    data class Challenge(
        @SerialName("mfa_status") val mfaStatus: String? = null,
        @SerialName("expires_at") val expiresAt: String? = null,
        @SerialName("remaining_attempts") val remainingAttempts: Int? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
        @SerialName("flow_id") val flowId: String? = null,
        @SerialName("remaining_retries") val remainingRetries: Int? = null,
        @SerialName("alternate_type") val alternateType: String? = null,
        @SerialName("id") val id: String? = null,
        @SerialName("type") val type: String? = null,
        @SerialName("user") val user: String? = null,
        @SerialName("status") val status: String? = null
    )
}

fun SignInChallengeResponse.mapToOtpChallenge(): SignInResult {
    return SignInResult.OtpChallenge(
        details = detail.orEmpty(),
        challengeId = challenge?.id.orEmpty(),
        expiresAt = challenge?.expiresAt.orEmpty(),
        status = challenge?.status.orEmpty()
    )
}

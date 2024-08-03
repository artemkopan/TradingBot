package io.trading.bot.network.robinhood.response

import io.trading.bot.model.AccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String? = null,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    @SerialName("scope")
    val scope: String? = null,
    @SerialName("token_type")
    val tokenType: String? = null,
    @SerialName("backup_code")
    val backupCode: String? = null,
    @SerialName("expires_in")
    val expiresIn: Int? = null,
    @SerialName("mfa_code")
    val mfaCode: String? = null
)


fun TokenResponse.toAccessToken(): AccessToken {
    return AccessToken(
        requireNotNull(accessToken) { "Access token is null" },
        requireNotNull(expiresIn) { "ExpiresIn is null" },
        requireNotNull(refreshToken) { "Refresh token is null" }
    )
}
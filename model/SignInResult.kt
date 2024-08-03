package io.trading.bot.model

sealed interface SignInResult {
    data class OtpChallenge(
        val details: String,
        val challengeId: String,
        val expiresAt: String,
        val status: String
    ) : SignInResult

    data class Token(val accessToken: AccessToken) : SignInResult
}
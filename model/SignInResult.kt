package io.trading.bot.model

data class SignInResult(
    val details: String,
    val challengeId: String,
    val expiresAt: String,
    val status: String,
)
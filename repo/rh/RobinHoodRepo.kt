package io.trading.bot.repo.rh

import io.trading.bot.model.AccessToken
import io.trading.bot.model.Quote
import io.trading.bot.model.SignInResult
import io.trading.bot.network.robinhood.RobinHoodNetwork
import org.koin.core.annotation.Factory


@Factory
class RobinHoodRepo(private val robinHoodNetwork: RobinHoodNetwork) {

    suspend fun signIn(
        email: String,
        password: String,
        deviceToken: String,
    ): Result<SignInResult> {
        return robinHoodNetwork.signIn(
            userName = email,
            password = password,
            deviceToken = deviceToken,
        )
    }

    suspend fun signInWithOtp(
        email: String,
        password: String,
        deviceToken: String,
        challengeId: String
    ): Result<AccessToken> {
        return robinHoodNetwork.signInWithOtp(
            userName = email,
            password = password,
            deviceToken = deviceToken,
            challengeId = challengeId
        )
    }

    suspend fun challenge(challengeId: String, otp: String): Result<Unit> {
        return robinHoodNetwork.challenge(challengeId, otp)
    }

    suspend fun refreshToken(it: AccessToken): Result<AccessToken> {
        return robinHoodNetwork.refreshToken(it.refreshToken)
    }

    suspend fun getQuote(stock: String): Result<Quote> {
        return robinHoodNetwork.getQuote(stock)
    }
}
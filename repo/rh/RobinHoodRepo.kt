package io.trading.bot.repo.rh

import io.trading.bot.model.SignInResult
import io.trading.bot.network.robinhood.RobinHoodNetwork
import org.koin.core.annotation.Factory


@Factory
class RobinHoodRepo(private val robinHoodNetwork: RobinHoodNetwork) {

    suspend fun signIn(email: String, password: String, deviceToken: String): Result<SignInResult> {
        return robinHoodNetwork.signIn(
            userName = email,
            password = password,
            deviceToken = deviceToken
        )
    }
}
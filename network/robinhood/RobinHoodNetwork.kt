package io.trading.bot.network.robinhood

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import io.trading.bot.model.SignInResult
import io.trading.bot.network.QUALIFIER_RH
import io.trading.bot.network.robinhood.response.SignInResponse
import io.trading.bot.service.SecretsProvider
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RobinHoodNetwork(
    @Named(QUALIFIER_RH) private val httpClient: HttpClient,
    private val secretsProvider: SecretsProvider
) {

    suspend fun signIn(
        userName: String,
        password: String,
        deviceToken: String,
        challengeType: String = "sms",
        expiresIn: Int = 86400
    ): Result<SignInResult> {
        return runCatching {
            httpClient.submitForm("/oauth2/token/", formParameters = parameters {
                append("username", userName)
                append("password", password)
                append("grant_type", "password")
                append("scope", "internal")
                append("challenge_type", challengeType)
                append("device_token", deviceToken)
                append("expires_in", expiresIn.toString())
                append("client_id", secretsProvider.robinHoodClientId())
            }).body<SignInResponse>()
        }.map {
            SignInResult(
                details = it.detail.orEmpty(),
                challengeId = it.challenge?.id.orEmpty(),
                expiresAt = it.challenge?.expiresAt.orEmpty(),
                status = it.challenge?.status.orEmpty()
            )
        }
    }
}
package io.trading.bot.network.robinhood

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import io.trading.bot.exception.AuthException
import io.trading.bot.model.AccessToken
import io.trading.bot.model.Quote
import io.trading.bot.model.SignInResult
import io.trading.bot.network.QUALIFIER_RH
import io.trading.bot.network.robinhood.request.ChallengeRequest
import io.trading.bot.network.robinhood.response.QuoteResponse
import io.trading.bot.network.robinhood.response.SignInChallengeResponse
import io.trading.bot.network.robinhood.response.TokenResponse
import io.trading.bot.network.robinhood.response.map
import io.trading.bot.network.robinhood.response.mapToOtpChallenge
import io.trading.bot.network.robinhood.response.toAccessToken
import io.trading.bot.service.SecretsProvider
import io.trading.bot.utils.flatten
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
        deviceToken: String
    ): Result<SignInResult> {
        return runCatching {
            httpClient.submitForm(
                PATH_OAUTH_TOKEN, formParameters = signInParams(
                    userName = userName,
                    password = password,
                    deviceToken = deviceToken
                )
            ) {
                setAttributes {
                    attributes.put(Auth.AuthCircuitBreaker, Unit)
                }
            }
        }.flatten { it.toSignInResult() }
    }

    suspend fun signInWithOtp(
        userName: String,
        password: String,
        deviceToken: String,
        challengeId: String
    ): Result<AccessToken> {
        return runCatching {
            httpClient.submitForm(
                PATH_OAUTH_TOKEN, formParameters = signInParams(
                    userName = userName,
                    password = password,
                    deviceToken = deviceToken,
                )
            ) {
                setAttributes {
                    attributes.put(Auth.AuthCircuitBreaker, Unit)
                }
                challengeId.takeIf { it.isNotBlank() }
                    ?.let { header("X-ROBINHOOD-CHALLENGE-RESPONSE-ID", it) }
            }.body<TokenResponse>()
        }.map {
            it.toAccessToken()
        }
    }

    private fun signInParams(
        userName: String,
        password: String,
        deviceToken: String,
    ) = parameters {
        append("username", userName)
        append("password", password)
        append("grant_type", "password")
        append("scope", "internal")
        append("challenge_type", CHALLENGE_TYPE)
        append("device_token", deviceToken)
        append("expires_in", EXPIRES_IN.toString())
        append("client_id", secretsProvider.robinHoodClientId())
    }

    suspend fun challenge(
        challengeId: String,
        otp: String
    ): Result<Unit> {
        return runCatching {
            httpClient.post("/challenge/$challengeId/respond/") {
                header("X-ROBINHOOD-CHALLENGE-RESPONSE-ID", challengeId)
                setBody(ChallengeRequest(response = otp))
                setAttributes {
                    attributes.put(Auth.AuthCircuitBreaker, Unit)
                }
            }.body<SignInChallengeResponse>()
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<AccessToken> {
        return runCatching {
            httpClient.submitForm(PATH_OAUTH_TOKEN, formParameters = parameters {
                append("grant_type", "refresh_token")
                append("refresh_token", refreshToken)
                append("scope", "internal")
                append("client_id", secretsProvider.robinHoodClientId())
                append("expires_in", EXPIRES_IN.toString())
            }) {
                setAttributes {
                    attributes.put(Auth.AuthCircuitBreaker, Unit)
                }
            }.body<TokenResponse>()
        }.map {
            it.toAccessToken()
        }
    }

    suspend fun getQuote(stock: String): Result<Quote> {
        return runCatching {
            httpClient.get("$PATH_QUOTES$stock/").body<QuoteResponse>()
        }.map { it.map() }
    }

    private suspend fun HttpResponse.toSignInResult(): Result<SignInResult> {
        val challengeResponse = body<SignInChallengeResponse>()
        if (challengeResponse.challenge == null) {
            val tokenResponse = body<TokenResponse>()
            if (tokenResponse.accessToken.isNullOrBlank()) {
                return Result.failure(AuthException("Filed to sign in"))
            } else {
                return Result.success(SignInResult.Token(tokenResponse.toAccessToken()))
            }
        } else {
            return Result.success(challengeResponse.mapToOtpChallenge())
        }
    }

    companion object {
        private const val CHALLENGE_TYPE: String = "sms"
        private const val EXPIRES_IN: Int = 86400

        private const val PATH_OAUTH_TOKEN = "/oauth2/token/"
        private const val PATH_QUOTES = "/quotes/"
    }
}
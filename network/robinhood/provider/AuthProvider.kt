package io.trading.bot.network.robinhood.provider

import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthConfig
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.trading.bot.model.AccessToken
import io.trading.bot.storage.prefs.PreferenceCache
import io.trading.bot.usecase.auth.RefreshAccessTokenUseCase
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class AuthProvider(
    private val preferenceCache: PreferenceCache,
    private val refreshAccessTokenUseCase: Lazy<RefreshAccessTokenUseCase>
) {

    fun config(authConfig: BearerAuthConfig) = with(authConfig) {
        loadTokens {
            val accessToken = preferenceCache.getToken().getOrNull()
            Timber.d("loadTokens: $accessToken")
            accessToken?.map()
        }
        refreshTokens {
            refreshAccessTokenUseCase.value().getOrThrow().map()
        }
        sendWithoutRequest { request ->
            request.attributes.contains(Auth.AuthCircuitBreaker).not()
        }
    }

    private fun AccessToken.map() = BearerTokens(token, refreshToken)
}
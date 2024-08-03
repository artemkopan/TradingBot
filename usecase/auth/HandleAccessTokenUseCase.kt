package io.trading.bot.usecase.auth

import io.trading.bot.model.AccessToken
import io.trading.bot.storage.prefs.PreferenceCache
import org.koin.core.annotation.Factory

@Factory
class HandleAccessTokenUseCase(
    private val preferenceCache: PreferenceCache
) {
    suspend operator fun invoke(accessToken: AccessToken): Result<Unit> {
        return runCatching {
            preferenceCache.setToken(accessToken)

            //todo schedule worker to refresh token
        }
    }
}
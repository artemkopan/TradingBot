package io.trading.bot.usecase.auth

import io.trading.bot.model.AccessToken
import io.trading.bot.repo.rh.RobinHoodRepo
import io.trading.bot.storage.prefs.PreferenceCache
import io.trading.bot.utils.flatten
import org.koin.core.annotation.Factory

@Factory
class RefreshAccessTokenUseCase(
    private val robinHoodRepo: RobinHoodRepo,
    private val preferenceCache: PreferenceCache
) {
    suspend operator fun invoke(): Result<AccessToken> {
        return preferenceCache.getToken()
            .flatten { robinHoodRepo.refreshToken(it) }
            .onSuccess { preferenceCache.setToken(it) }
    }
}
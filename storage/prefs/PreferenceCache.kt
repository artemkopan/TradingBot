package io.trading.bot.storage.prefs

import io.trading.bot.exception.AuthException
import io.trading.bot.model.AccessToken
import org.koin.core.annotation.Factory

private const val KEY_TOKEN = "token"

@Factory
class PreferenceCache(private val preferenceStorage: PreferenceStorage) {

    suspend fun setToken(token: AccessToken): Result<Unit> {
        return runCatching { preferenceStorage.setData(KEY_TOKEN, token) }
    }

    suspend fun getToken(): Result<AccessToken> {
        return preferenceStorage.getData<AccessToken>(KEY_TOKEN)?.let {
            Result.success(it)
        } ?: Result.failure(AuthException("No token found"))
    }
}
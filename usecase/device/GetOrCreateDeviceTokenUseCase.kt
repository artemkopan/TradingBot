package io.trading.bot.usecase.device

import io.trading.bot.storage.prefs.PreferenceStorage
import org.koin.core.annotation.Factory
import java.util.UUID

@Factory
class GetOrCreateDeviceTokenUseCase(private val preferenceStorage: PreferenceStorage) {

    suspend operator fun invoke(): String {
        val deviceToken = preferenceStorage.getValue("device_token")
        if (deviceToken != null) {
            return deviceToken
        }
        val newDeviceToken = UUID.randomUUID().toString()
        preferenceStorage.setValue("device_token", newDeviceToken)
        return newDeviceToken
    }
}
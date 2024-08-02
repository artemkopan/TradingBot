package io.trading.bot.service

import io.trading.bot.BuildConfig
import org.koin.core.annotation.Factory

@Factory
class SecretsProvider {

    fun telegramChatId(): String = BuildConfig.telegramChataId
    fun telegramBotId(): String = BuildConfig.telegramBotId
    fun robinHoodClientId(): String = BuildConfig.robinhoodClientId
}
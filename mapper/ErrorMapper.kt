package io.trading.bot.mapper

import org.koin.core.annotation.Single

@Single
class ErrorMapper {

    operator fun invoke(throwable: Throwable): String =
        throwable.message.orEmpty() + '\n' + throwable.stackTraceToString()
}
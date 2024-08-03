package io.trading.bot.utils

inline fun <T, R> Result<T>.flatten(transform: (T) -> Result<R>): Result<R> {
    return mapCatching { transform(it).getOrThrow() }
}
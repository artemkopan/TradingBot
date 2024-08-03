package io.trading.bot.network.robinhood.response

import io.trading.bot.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class QuoteResponse(

    @SerialName("last_trade_price_source")
    val lastTradePriceSource: String? = null,

    @SerialName("last_trade_price")
    val lastTradePrice: String? = null,

    @SerialName("symbol")
    val symbol: String? = null,

    @SerialName("ask_size")
    val askSize: Int? = null,

    @SerialName("venue_bid_time")
    val venueBidTime: String? = null,

    @SerialName("venue_last_trade_time")
    val venueLastTradeTime: String? = null,

    @SerialName("venue_last_non_reg_trade_time")
    val venueLastNonRegTradeTime: String? = null,

    @SerialName("has_traded")
    val hasTraded: Boolean? = null,

    @SerialName("instrument")
    val instrument: String? = null,

    @SerialName("adjusted_previous_close")
    val adjustedPreviousClose: String? = null,

    @SerialName("previous_close_date")
    val previousCloseDate: String? = null,

    @SerialName("instrument_id")
    val instrumentId: String? = null,

    @SerialName("ask_price")
    val askPrice: String? = null,

    @SerialName("venue_ask_time")
    val venueAskTime: String? = null,

    @SerialName("trading_halted")
    val tradingHalted: Boolean? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("bid_size")
    val bidSize: Int? = null,

    @SerialName("previous_close")
    val previousClose: String? = null,

    @SerialName("last_non_reg_trade_price")
    val lastNonRegTradePrice: String? = null,

    @SerialName("last_extended_hours_trade_price")
    val lastExtendedHoursTradePrice: String? = null,

    @SerialName("state")
    val state: String? = null,

    @SerialName("bid_price")
    val bidPrice: String? = null,

    @SerialName("last_non_reg_trade_price_source")
    val lastNonRegTradePriceSource: String? = null
)

fun QuoteResponse.map(): Quote {
    return Quote(
        stock = symbol.orEmpty(),
        lastPrice = lastTradePrice.orEmpty()
    )
}

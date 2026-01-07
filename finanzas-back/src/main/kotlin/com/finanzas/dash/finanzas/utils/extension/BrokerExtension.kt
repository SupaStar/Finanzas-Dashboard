package com.finanzas.dash.finanzas.utils.extension

import com.finanzas.dash.finanzas.dto.response.stock.StockDto
import com.finanzas.dash.finanzas.entity.Stock

fun Stock.toDto() = StockDto(
    stockId = this.stockId!!,
    symbol = this.symbol!!,
    broker = this.broker?.name!!,
    closeDay = this.closeDay!!,
    lastFetch = this.lastFetch!!,
    currency = this.currency!!,
    name = this.name!!,
)
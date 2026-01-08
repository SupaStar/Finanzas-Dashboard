package com.finanzas.dash.finanzas.utils.extension

import com.finanzas.dash.finanzas.dto.response.broker.BrokerDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioDto
import com.finanzas.dash.finanzas.dto.response.stock.StockDto
import com.finanzas.dash.finanzas.entity.Broker
import com.finanzas.dash.finanzas.entity.Portfolio
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

fun Portfolio.toDto() = PortfolioDto(
    portfolioId = this.portfolioId!!,
    Stock = this.stock!!.toDto(),
    avgPrice = this.avgPrice!!,
    totalQuantity = this.totalQuantity!!
)

fun Broker.toDto() = BrokerDto(
    brokerId = this.brokerId!!,
    name = this.name!!,
    symbol = this.symbol!!
)
package com.finanzas.dash.finanzas.utils.extension

import com.finanzas.dash.finanzas.dto.response.broker.BrokerDto
import com.finanzas.dash.finanzas.dto.response.dividend.DividendDto
import com.finanzas.dash.finanzas.dto.response.operation.OperationDto
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioDto
import com.finanzas.dash.finanzas.dto.response.stock.StockDto
import com.finanzas.dash.finanzas.entity.Broker
import com.finanzas.dash.finanzas.entity.Dividend
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.entity.Stock
import com.finanzas.dash.finanzas.dto.response.portfolio.PortfolioGeneralInformationDto
import com.finanzas.dash.finanzas.entity.PortfolioGeneralInformation
fun Stock.toDto() = StockDto(
    stockId = this.stockId!!,
    symbol = this.symbol!!,
    broker = this.broker?.name!!,
    closeDay = this.closeDay!!,
    lastFetch = this.lastFetch!!,
    currency = this.currency!!,
    name = this.name!!,
)

fun PortfolioGeneralInformation.toDto() = PortfolioGeneralInformationDto(
    portfolioGeneralInformationId = this.portfolioGeneralInformationId!!,
    year = this.year!!,
    month = this.month!!,
    dividendsTotal = this.dividendsTotal!!,
)

fun Portfolio.toDto() = PortfolioDto(
    portfolioId = this.portfolioId!!,
    Stock = this.stock!!.toDto(),
    avgPrice = this.avgPrice!!,
    totalQuantity = this.totalQuantity!!,
    nOperations = this.nOperations ?: 0,
    generalInformation = this.generalInformation.map { it.toDto() },
)

fun Portfolio.toDtoLight() = PortfolioDto(
    portfolioId = this.portfolioId!!,
    Stock = this.stock!!.toDto(),
    avgPrice = this.avgPrice!!,
    totalQuantity = this.totalQuantity!!,
    nOperations = this.nOperations ?: 0
)

fun Broker.toDto() = BrokerDto(
    brokerId = this.brokerId!!,
    name = this.name!!,
    symbol = this.symbol!!
)

fun Operation.toDto() = OperationDto(
    operationId = this.operationId!!,
    operationType = this.operationType!!,
    quantity = this.quantity!!,
    price = this.price!!,
    fee = this.fee!!,
    tax = this.tax!!,
    total = this.total!!,
    operationDate = this.operationDate!!,
)

fun Dividend.toDto() = DividendDto(
    dividendId = this.dividendId!!,
    dividendType = this.dividendType!!,
    value = this.value!!,
    portfolio = this.portfolio!!.toDtoLight(),
    paidDate = this.paidDate!!,
    currencyCode = this.currencyCode!!,
    tax = this.tax,
    netValue = this.netValue!!,
    exchangeRate = this.exchangeRate!!,
)
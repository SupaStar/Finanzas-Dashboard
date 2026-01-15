package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.dto.request.stocks.AddStockRequestDto
import com.finanzas.dash.finanzas.entity.Operation
import com.finanzas.dash.finanzas.entity.Portfolio
import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.enum.OperationTypeEnum
import com.finanzas.dash.finanzas.repository.OperationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.repository.StockRepository
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

@Service
class CsvService(
    private val securityService: SecurityService,
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val operationRepository: OperationRepository,
    private val stockService: StockService,
    private val portfolioService: PortfolioService,
) {
    @Transactional
    fun processCsv(file: MultipartFile) {
        try {
            val user = securityService.currentUser()

            val stocks = stockRepository.findAll()

            val inputStream = file.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val csvReader = CSVReader(reader)
            csvReader.skip(1)

            val filas = csvReader.readAll()
            var operations = mutableListOf<Operation>()
            val portfoliosId = mutableSetOf<Long>()
            filas.forEach { fila ->
                val operationDate = fila[0]
                val date = LocalDate.parse(operationDate)
                val accion = fila[1].replace(" ", "").uppercase()
                val quantity = fila[2].toBigDecimal()
                val price = fila[3].toBigDecimal()
                val fee = fila[4].toBigDecimal()
                val symbol = fila[6]
                val portfolio = validateOrCreatePortfolio(accion, user)
                val operation = Operation().apply {
                    this.operationType = OperationTypeEnum.buy
                    this.quantity = quantity
                    this.price = price
                    this.portfolio = portfolio
                    this.fee = fee
                    this.tax = BigDecimal.ZERO
                    this.total = quantity * price
                    this.operationDate = date.atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
                }
                portfoliosId.add(portfolio.portfolioId!!)
                operations.add(operation)
            }
            operationRepository.saveAll(operations)
//            portfoliosId.forEach { portfolioId ->
//                portfolioService.updatePortfolioData(portfolioId)
//            }

        } catch (e: CsvException) {
            e.printStackTrace()
        }
    }
    @Transactional
    fun updateAllPortfolios() {
        try{
            val user = securityService.currentUser()
            val portfolios = portfolioRepository.findByUserUserId(user.userId!!)
            portfolios.forEach { portfolio ->
                portfolioService.updatePortfolioData(portfolio.portfolioId!!)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun validateOrCreatePortfolio(stockName: String, user: User): Portfolio {
        val existingPortfolio = portfolioRepository.findByUserUserIdAndStockSymbol(user.userId!!, stockName)
        if (existingPortfolio != null) {
            return existingPortfolio
        }
        val stock =
            stockRepository.findBySymbol(stockName) ?: throw RuntimeException("Stock with name $stockName not found")
        val newPortfolio = Portfolio().apply {
            this.stock = stock
            this.user = user
            this.avgPrice = BigDecimal.ZERO
            this.totalQuantity = BigDecimal.ZERO
        }
        return portfolioRepository.save(newPortfolio)
    }

    fun addStocks(file: MultipartFile) {
        try {
            val inputStream = file.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val csvReader = CSVReader(reader)
            csvReader.skip(1)

            val filas = csvReader.readAll()
            filas.forEach { fila ->
                val stockName = fila[0].replace(" ", "")
                val brokerId = fila[1].toLong()
                val request = AddStockRequestDto(
                    stockName = stockName,
                    brokerId = brokerId
                )
                stockService.addStockSimple(request)
            }
        } catch (e: CsvException) {
            e.printStackTrace()
        }
    }
}
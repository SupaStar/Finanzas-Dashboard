package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.dto.MasDividendosResponseDto
import com.finanzas.dash.finanzas.entity.DividendAnnouncement
import com.finanzas.dash.finanzas.entity.SyncState
import com.finanzas.dash.finanzas.enum.DividendTypeEnum
import com.finanzas.dash.finanzas.repository.DividendAnnouncementRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import com.finanzas.dash.finanzas.repository.SyncStateRepository
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class MasDividendosService(
    private val syncStateRepository: SyncStateRepository,
    private val dividendAnnouncementRepository: DividendAnnouncementRepository,
    private val portfolioRepository: PortfolioRepository,
    private val notificationService: NotificationService
) {
    private val log = LoggerFactory.getLogger(MasDividendosService::class.java)
    private val restTemplate = RestTemplate()
    private val apiUrl = "https://app.masdividendos.mx/config/conexiones/database.php"
    private val syncKey = "mas_dividendos"

    @Transactional
    fun syncDividends() {
        log.info("Starting MasDividendos sync process")
        
        val syncState = syncStateRepository.findById(syncKey).orElse(null)
        val lastIdProcessed = syncState?.lastIdProcessed ?: 0L

        val responseType = object : ParameterizedTypeReference<List<MasDividendosResponseDto>>() {}
        
        try {
            val response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, responseType)
            val allDividends = response.body ?: emptyList()

            log.info("Fetched ${allDividends.size} dividend records from API")

            var maxIdProcessed = lastIdProcessed

            val isFirstRun = (lastIdProcessed == 0L)
            val today = LocalDate.now()

            val newDividendsToSave = mutableListOf<DividendAnnouncement>()

            for (dto in allDividends) {
                val sourceIdStr = dto.id ?: continue
                val sourceId = sourceIdStr.toLongOrNull() ?: continue

                if (sourceId <= lastIdProcessed) {
                    continue
                }

                val parsedPayDate = parseDate(dto.fechaPago)
                if (isFirstRun && parsedPayDate != null && parsedPayDate.isBefore(today)) {
                    if (sourceId > maxIdProcessed) {
                        maxIdProcessed = sourceId
                    }
                    continue
                }

                val type = determineDividendType(dto.comentario)
                val cleanAmount = cleanAmountStr(dto.monto)

                if (cleanAmount == null || dto.ticker.isNullOrBlank()) {
                    if (sourceId > maxIdProcessed) {
                        maxIdProcessed = sourceId
                    }
                    continue
                }

                val entity = DividendAnnouncement(
                    sourceId = sourceId,
                    ticker = dto.ticker,
                    company = dto.empresa,
                    amount = cleanAmount,
                    comment = dto.comentario,
                    type = type,
                    payDate = parsedPayDate,
                    exDate = parseDate(dto.fechaExDerecho),
                    link = dto.linkAviso
                )

                newDividendsToSave.add(entity)
                
                notifyUsersForTicker(entity)

                if (sourceId > maxIdProcessed) {
                    maxIdProcessed = sourceId
                }
            }

            if (newDividendsToSave.isNotEmpty()) {
                dividendAnnouncementRepository.saveAll(newDividendsToSave)
                log.info("Saved ${newDividendsToSave.size} new dividend announcements.")
            } else {
                log.info("No new dividend announcements to save.")
            }

            if (maxIdProcessed > lastIdProcessed) {
                val updatedSyncState = syncState?.apply { this.lastIdProcessed = maxIdProcessed }
                    ?: SyncState(syncKey = syncKey, lastIdProcessed = maxIdProcessed)
                syncStateRepository.save(updatedSyncState)
                log.info("Updated sync state lastIdProcessed to $maxIdProcessed")
            }

        } catch (e: Exception) {
            log.error("Error occurred while syncing MasDividendos API", e)
            throw e
        }
    }

    private fun notifyUsersForTicker(announcement: DividendAnnouncement) {
        val portfolios = portfolioRepository.findByStockSymbol(announcement.ticker)
        for (portfolio in portfolios) {
            val user = portfolio.user ?: continue
            val nTitulos = portfolio.totalQuantity ?: BigDecimal.ZERO
            
            if (nTitulos <= BigDecimal.ZERO) continue

            val monto = announcement.amount
            val baseTotal = nTitulos.multiply(monto)
            
            val message = when (announcement.type) {
                DividendTypeEnum.cash -> {
                    val montoCalculado = baseTotal.multiply(BigDecimal("0.7"))
                    val impuestoCalculado = baseTotal.multiply(BigDecimal("0.3"))
                    "Se anunció un nuevo pago para tu acción ${announcement.ticker} de monto ${monto.toPlainString()}, por tus ${nTitulos.toPlainString()} títulos obtendrás un aproximado de ${montoCalculado.toPlainString()}. El impuesto aprox (ISR 30%) es de ${impuestoCalculado.toPlainString()}."
                }
                DividendTypeEnum.reinvested -> {
                    val montoCalculado = baseTotal
                    "Se anunció un nuevo pago para tu acción ${announcement.ticker} de monto ${monto.toPlainString()}, por tus ${nTitulos.toPlainString()} títulos obtendrás un aproximado de ${montoCalculado.toPlainString()}."
                }
                DividendTypeEnum.UNDEFINED -> {
                    "Se anunció un nuevo pago para tu acción ${announcement.ticker} de monto ${monto.toPlainString()}, el anuncio aún no dice qué concepto tendrá por lo que no se puede calcular un aproximado exacto."
                }
            }
            
            notificationService.createNotification(user, message, announcement.link)
        }
    }

    private fun determineDividendType(comment: String?): DividendTypeEnum {
        if (comment == null) return DividendTypeEnum.UNDEFINED
        
        val normalizedComment = comment.lowercase()
        return if (normalizedComment.contains("resultado fiscal")) {
            DividendTypeEnum.cash
        } else if (normalizedComment.contains("retorno de capital") || normalizedComment.contains("reembolso de capital")) {
            DividendTypeEnum.reinvested
        } else {
            DividendTypeEnum.UNDEFINED
        }
    }

    private fun cleanAmountStr(amountStr: String?): BigDecimal? {
        if (amountStr.isNullOrBlank()) return null
        
        if (amountStr.contains(Regex("[a-zA-Z]"))) {
            log.warn("Amount contains text, omitting record: {}", amountStr)
            return null
        }
        
        val cleanStr = amountStr.replace("$", "").replace(",", "").trim()
        return try {
            BigDecimal(cleanStr)
        } catch (e: NumberFormatException) {
            log.warn("Could not parse amount: {}", amountStr)
            null
        }
    }

    private fun parseDate(dateStr: String?): LocalDate? {
        if (dateStr.isNullOrBlank()) return null
        return try {
            LocalDate.parse(dateStr)
        } catch (e: DateTimeParseException) {
            log.warn("Could not parse date: {}", dateStr)
            null
        }
    }
}

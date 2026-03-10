package com.finanzas.dash.finanzas.service

import com.finanzas.dash.finanzas.entity.PortfolioGeneralInformation
import com.finanzas.dash.finanzas.repository.PortfolioGeneralInformationRepository
import com.finanzas.dash.finanzas.repository.PortfolioRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PortfolioGeneralInformationService(
    private val portfolioGeneralInformationRepository: PortfolioGeneralInformationRepository,
    private val portfolioRepository: PortfolioRepository
) {
    private val log = LoggerFactory.getLogger(PortfolioGeneralInformationService::class.java)

    @Transactional
    fun calculateAndSaveGeneralInformation() {
        val startTime = System.currentTimeMillis()
        System.out.println("Iniciando batch calculation de Portfolio General Information en la base de datos...")

        System.out.println("1/2 Ejecutando UPDATE de contadores de operaciones en portafolios (Global)...")
        portfolioRepository.updatePortfolioOperationsCount()

        System.out.println("2/2 Ejecutando UPSERT de totales de dividendos en metadata...")
        portfolioGeneralInformationRepository.upsertDividendsTotal()

        val timeTaken = System.currentTimeMillis() - startTime
        System.out.println("Calculo batch de Portfolio completado exitosamente en ${timeTaken} ms.")
    }
}

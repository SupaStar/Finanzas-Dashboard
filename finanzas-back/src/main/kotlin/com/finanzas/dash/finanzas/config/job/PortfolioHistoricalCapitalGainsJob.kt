package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.PortfolioHistoricalCapitalGainsService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class PortfolioHistoricalCapitalGainsJob(private val service: PortfolioHistoricalCapitalGainsService) : Job {
    private val log = LoggerFactory.getLogger(PortfolioHistoricalCapitalGainsJob::class.java)

    override fun execute(context: JobExecutionContext?) {
        log.info("Job disparado: Calculo historico general de plusvalia de portafolios.")
        service.calculateHistoricalCapitalGains()
        log.info("Job finalizado: Calculo historico general de plusvalia de portafolios.")
    }
}

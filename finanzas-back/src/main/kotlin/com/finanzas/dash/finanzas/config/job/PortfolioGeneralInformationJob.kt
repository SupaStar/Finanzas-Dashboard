package com.finanzas.dash.finanzas.config.job

import com.finanzas.dash.finanzas.service.PortfolioGeneralInformationService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory

class PortfolioGeneralInformationJob(private val service: PortfolioGeneralInformationService) : Job {
    private val log = LoggerFactory.getLogger(PortfolioGeneralInformationJob::class.java)

    override fun execute(context: JobExecutionContext?) {
        log.info("Job disparado: Calculo de informacion general de portafolios.")
        service.calculateAndSaveGeneralInformation()
        log.info("Job finalizado: Calculo de informacion general de portafolios.")
    }
}

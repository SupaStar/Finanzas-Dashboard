package com.finanzas.dash.finanzas.config

import com.finanzas.dash.finanzas.config.job.ActualizarPrecios
import com.finanzas.dash.finanzas.config.job.DailyFixedPortfolioInterestJob
import com.finanzas.dash.finanzas.config.job.PortfolioGeneralInformationJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuartzConfig {

    @Bean
    fun jobDetail(): JobDetail {
        return JobBuilder.newJob(ActualizarPrecios::class.java)
            .withIdentity("tareaActualizarPrecios", "stock")
            .storeDurably() // Importante para que Spring lo maneje
            .build()
    }

    @Bean
    fun jobTrigger(jobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity("triggerHorarioOficina", "stock")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 7-16 ? * MON-FRI"))
            .build()
    }

    @Bean
    fun portfolioInfoJobDetail(): JobDetail {
        return JobBuilder.newJob(PortfolioGeneralInformationJob::class.java)
            .withIdentity("tareaCalculoInfoPortafolio", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun portfolioInfoJobTrigger(portfolioInfoJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(portfolioInfoJobDetail)
            .withIdentity("triggerCalculoDiario", "portfolio")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ?")) // 2:00 AM every day
            .build()
    }

    @Bean
    fun dailyInterestJobDetail(): JobDetail {
        return JobBuilder.newJob(DailyFixedPortfolioInterestJob::class.java)
            .withIdentity("tareaCalculoInteresDiario", "portfolio")
            .storeDurably()
            .build()
    }

    @Bean
    fun dailyInterestJobTrigger(dailyInterestJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(dailyInterestJobDetail)
            .withIdentity("triggerCalculoInteresDiario", "portfolio")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 30 0 * * ?")) // 12:30 AM every day
            .build()
    }
}
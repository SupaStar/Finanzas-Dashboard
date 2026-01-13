package com.finanzas.dash.finanzas.config

import com.finanzas.dash.finanzas.config.job.ActualizarPrecios
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
}
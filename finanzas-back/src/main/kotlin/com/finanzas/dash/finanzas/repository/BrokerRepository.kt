package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.Broker
import org.springframework.data.jpa.repository.JpaRepository

interface BrokerRepository : JpaRepository<Broker, Int> {
    fun findByBrokerId(brokerId: Long): Broker?
}
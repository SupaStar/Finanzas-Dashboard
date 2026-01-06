package com.finanzas.dash.finanzas.db.seeder

import com.finanzas.dash.finanzas.entity.Broker
import com.finanzas.dash.finanzas.repository.BrokerRepository
import org.springframework.stereotype.Component

@Component
class BrokerSeeder(private val brokerRepository: BrokerRepository) : DatabaseSeeder {
    override fun seed() {
        if (brokerRepository.count() == 0L) {
            brokerRepository.save(Broker().apply {
                this.name = "CETES"
                this.symbol = "CETES"
            })
            brokerRepository.save(Broker().apply {
                this.name = "Bolsa Mexicana de Valores"
                this.symbol = "BMV"
            })
            brokerRepository.save(Broker().apply {
                this.name = "Bolsa Institucional de Valores:"
                this.symbol = "BIVA"
            })
            brokerRepository.save(Broker().apply {
                this.name = "NYSE Arca"
                this.symbol = "NYSEARCA"
            })
            brokerRepository.save(Broker().apply {
                this.name = "Bolsa de Nueva York"
                this.symbol = "NYSE"
            })
        }
    }
}
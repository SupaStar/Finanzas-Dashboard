package com.finanzas.dash.finanzas.db.seeder

import com.finanzas.dash.finanzas.entity.Broker
import com.finanzas.dash.finanzas.entity.FixedInstrument
import com.finanzas.dash.finanzas.repository.FixedInstrumentRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class FixedInstrumentSeeder(private val fixedInstrumentRepository: FixedInstrumentRepository):DatabaseSeeder {
    override fun seed() {
        if (fixedInstrumentRepository.count() == 0L) {
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "CETES bonddia"
                this.anualRate = BigDecimal.valueOf(6.88)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "NU Turbo"
                this.anualRate = BigDecimal.valueOf(13.00)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "NU Normal"
                this.anualRate = BigDecimal.valueOf(13.00)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "Revolut"
                this.anualRate = BigDecimal.valueOf(15.00)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "Revolut Parejas"
                this.anualRate = BigDecimal.valueOf(7.00)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "OpenBank"
                this.anualRate = BigDecimal.valueOf(13.00)
            })
            fixedInstrumentRepository.save(FixedInstrument().apply {
                this.name = "OpenBank Rest"
                this.anualRate = BigDecimal.valueOf(7.3)
            })
        }
    }
}
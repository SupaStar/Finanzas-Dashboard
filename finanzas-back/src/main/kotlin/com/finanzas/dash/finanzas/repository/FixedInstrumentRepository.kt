package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.FixedInstrument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixedInstrumentRepository : JpaRepository<FixedInstrument, Long>

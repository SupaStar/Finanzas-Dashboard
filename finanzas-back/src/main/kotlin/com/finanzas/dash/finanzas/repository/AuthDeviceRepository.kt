package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.AuthDevice
import org.springframework.data.jpa.repository.JpaRepository

interface AuthDeviceRepository: JpaRepository<AuthDevice, Int> {
}
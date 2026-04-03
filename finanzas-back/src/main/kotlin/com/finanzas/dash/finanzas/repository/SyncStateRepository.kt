package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.SyncState
import org.springframework.data.jpa.repository.JpaRepository

interface SyncStateRepository : JpaRepository<SyncState, String>

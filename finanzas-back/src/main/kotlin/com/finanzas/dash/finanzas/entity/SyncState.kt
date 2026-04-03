package com.finanzas.dash.finanzas.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sync_state")
data class SyncState(
    @Id
    val syncKey: String = "",
    
    var lastIdProcessed: Long = 0L
)

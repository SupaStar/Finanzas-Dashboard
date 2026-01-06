package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.time.OffsetDateTime


@Entity
@Table(name = "auth_devices")
class AuthDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val authDeviceId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private val auth: UserAuth? = null

    @Column(nullable = false)
    private var ip: String? = null

    private val deviceId: String? = null

    @Column(columnDefinition = "TEXT")
    private var userAgent: String? = null

    private val lastUsedAt: OffsetDateTime? = null
}

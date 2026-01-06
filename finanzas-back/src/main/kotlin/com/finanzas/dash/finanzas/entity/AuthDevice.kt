package com.finanzas.dash.finanzas.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.net.InetAddress
import java.time.OffsetDateTime


@Entity
@Table(name = "auth_devices")
class AuthDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val authDeviceId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    var auth: UserAuth? = null

    @Column(nullable = false)
    @JsonIgnore
    var ip: InetAddress? = null

    @JsonIgnore
    var deviceId: String? = null

    @Column(columnDefinition = "TEXT")
    var userAgent: String? = null

    var lastUsedAt: OffsetDateTime? = null
}

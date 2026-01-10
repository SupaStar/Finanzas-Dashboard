package com.finanzas.dash.finanzas.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.finanzas.dash.finanzas.enum.ProviderEnum
import jakarta.persistence.*
import java.time.OffsetDateTime


@Entity
@Table(name = "user_auth")
class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val authId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @JsonIgnore
    @Column(nullable = false)
    var passwordHash: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var provider: ProviderEnum? = ProviderEnum.local

    @JsonIgnore
    var lastLoginAt: OffsetDateTime? = null
    @JsonIgnore
    var passwordUpdatedAt: OffsetDateTime? = null
}
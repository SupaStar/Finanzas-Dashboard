package com.finanzas.dash.finanzas.entity

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
    private val user: User? = null

    @Column(nullable = false)
    private var passwordHash: String? = null

    @Column(nullable = false, length = 20)
    private var provider: ProviderEnum? = ProviderEnum.LOCAL

    private val lastLoginAt: OffsetDateTime? = null
    private val passwordUpdatedAt: OffsetDateTime? = null
}
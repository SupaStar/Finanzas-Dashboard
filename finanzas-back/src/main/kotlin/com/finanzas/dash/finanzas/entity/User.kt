package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.UserStatusEnum
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Int? = null

    @Column(nullable = false, unique = true, length = 100)
    var username: String? = null
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: UserStatusEnum? = UserStatusEnum.active

    @Column(nullable = false)
    var created_at: OffsetDateTime = OffsetDateTime.now()
}
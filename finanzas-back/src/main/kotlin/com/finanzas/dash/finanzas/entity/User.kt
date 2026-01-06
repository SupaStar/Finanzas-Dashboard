package com.finanzas.dash.finanzas.entity

import com.finanzas.dash.finanzas.enum.UserStatusEnum
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "app_user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var user_id: Int? = null

    @Column(nullable = false, unique = true, length = 100)
    var username: String? = null

    @Column(nullable = false, length = 20)
    var status: UserStatusEnum? = UserStatusEnum.ACTIVE

    @Column(nullable = false)
    var created_at: OffsetDateTime = OffsetDateTime.now()
}
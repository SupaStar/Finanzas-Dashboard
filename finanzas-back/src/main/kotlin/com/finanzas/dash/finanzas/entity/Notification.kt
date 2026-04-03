package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "notifications")
class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var message: String = ""

    @Column(length = 1000)
    var link: String? = null

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()
}

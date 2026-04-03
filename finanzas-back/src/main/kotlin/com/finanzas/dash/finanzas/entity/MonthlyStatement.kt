package com.finanzas.dash.finanzas.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "monthly_statements")
class MonthlyStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @Column(nullable = false)
    var month: Int = 0

    @Column(nullable = false)
    var year: Int = 0

    @Column(name = "file_path", nullable = false, length = 255)
    var filePath: String? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()
}

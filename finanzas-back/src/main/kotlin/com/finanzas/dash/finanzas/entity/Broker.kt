package com.finanzas.dash.finanzas.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "broker")
class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val brokerId: Long? = null

    @Column(nullable = false, length = 100)
    var name: String? = null

    @Column(nullable = false, unique = true, length = 50)
    var symbol: String? = null
}

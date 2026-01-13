package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByUserId(userId: Int): User?
    fun findByUsername(username: String): User?
}
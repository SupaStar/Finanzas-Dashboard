package com.finanzas.dash.finanzas.repository

import com.finanzas.dash.finanzas.entity.UserAuth
import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthRepository: JpaRepository<UserAuth, Int> {

    fun findByUserUserId(userId: Long): UserAuth?
}
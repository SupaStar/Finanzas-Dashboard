package com.finanzas.dash.finanzas.db.seeder

import com.finanzas.dash.finanzas.config.Encrypter
import com.finanzas.dash.finanzas.entity.AuthDevice
import com.finanzas.dash.finanzas.entity.User
import com.finanzas.dash.finanzas.entity.UserAuth
import com.finanzas.dash.finanzas.enum.ProviderEnum
import com.finanzas.dash.finanzas.repository.AuthDeviceRepository
import com.finanzas.dash.finanzas.repository.UserAuthRepository
import com.finanzas.dash.finanzas.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.time.OffsetDateTime

@Component
class UserSeeder(
    private val encrypter: Encrypter,
    private val userRepository: UserRepository,
    private val userAuthRepository: UserAuthRepository,
    private val authDeviceRepository: AuthDeviceRepository
) : DatabaseSeeder {

    override fun seed() {
        if (userRepository.count() == 0L) {
            val user = userRepository.save(User().apply {
                this.username = "SupaStar"
            })
            val userAuth = userAuthRepository.save(
                UserAuth().apply {
                    this.user = user
                    this.passwordHash = encrypter.encrypt("obednoe1")
                    this.provider = ProviderEnum.local
                    this.lastLoginAt = OffsetDateTime.now()
                    this.passwordUpdatedAt = OffsetDateTime.now()
                });

            authDeviceRepository.save(
                AuthDevice().apply {
                    this.auth = userAuth
                    this.platformName = "server"
                    this.lastUsedAt = OffsetDateTime.now()
                })
        }
    }
}
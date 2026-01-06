package com.finanzas.dash.finanzas.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class Encrypter(private val encrypter: PasswordEncoder = BCryptPasswordEncoder()) {
    fun encrypt(password: String): String {
        return encrypter.encode(password)!!
    }

    fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return encrypter.matches(rawPassword, encodedPassword)
    }
}
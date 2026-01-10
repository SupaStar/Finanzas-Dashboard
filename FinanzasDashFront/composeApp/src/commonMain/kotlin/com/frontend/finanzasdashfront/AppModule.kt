package com.frontend.finanzasdashfront

import com.frontend.finanzasdashfront.api.auth.AuthService
import com.frontend.finanzasdashfront.api.getEngine
import com.frontend.finanzasdashfront.config.SecurityManager
import com.frontend.finanzasdashfront.config.TokenManager
import com.frontend.finanzasdashfront.viewmodel.auth.LoginViewModel
import com.frontend.finanzasdashfront.viewmodel.dashboard.DashboardViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private val httpClient = HttpClient(getEngine()) { // getEngine() es el expect/actual de motores
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR_LOG: $message") // Verás esto en el Logcat o consola
                }
            }
        }
    }

    val authService = AuthService(httpClient)
    val tokenManager = TokenManager(SecurityManager())

    // 3. Proveemos el ViewModel
    fun provideLoginViewModel() = LoginViewModel(authService, tokenManager)
    fun provideDashboardViewModel() = DashboardViewModel(tokenManager)
}
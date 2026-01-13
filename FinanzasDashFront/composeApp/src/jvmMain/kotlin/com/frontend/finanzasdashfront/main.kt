package com.frontend.finanzasdashfront

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

actual fun isDebugBuild(): Boolean {
    // En JVM, puedes verificar propiedades del sistema o usar una constante
    return System.getProperty("debug")?.toBoolean() ?: true // Por defecto true para desarrollo
}

fun main() = application {
    val windowState = rememberWindowState(width = 1200.dp, height = 800.dp)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Finanzas Control",
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}
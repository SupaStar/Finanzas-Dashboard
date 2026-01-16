package com.frontend.finanzasdashfront.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.frontend.finanzasdashfront.ui.theme.*

val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = DarkGrey,
    surface = Surface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.White,
    onBackground = Color.White,
)

 val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Color.White,
    surface = Color.White
)
package com.example.goingmerry.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightColors = lightColors(
    primary = Color(0xFF59CAFF),
    primaryVariant = Color(0xFF5977FF),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF59ADFF),
    secondaryVariant = Color(0xFFB3C5FF),
    onSecondary = Color(0xFF686868),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

val darkColors = darkColors()

@Composable
fun NewGoingMerryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        lightColors
    } else {
        darkColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
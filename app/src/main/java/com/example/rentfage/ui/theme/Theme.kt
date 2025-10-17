package com.example.rentfage.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores para el tema claro, usando nuestros nuevos rojos.
private val LightColorScheme = lightColorScheme(
    primary = Red40,           // El color principal (AppTopBar, botones) será nuestro rojo elegante.
    secondary = LightRed40,
    tertiary = RedGrey40
)

// Paleta de colores para el tema oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = Red80,           // El color principal en modo oscuro.
    secondary = LightRed80,
    tertiary = RedGrey80
)

@Composable
fun RentfageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se cambia a `false` para desactivar el color dinámico y forzar nuestra paleta de rojos.
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

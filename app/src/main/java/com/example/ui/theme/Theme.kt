package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FasalGreenDarkTheme,
    onPrimary = Color(0xFF003915),
    primaryContainer = FasalGreenDark,
    onPrimaryContainer = FasalGreenContainer,
    secondary = FasalOrangeDarkTheme,
    onSecondary = Color(0xFF4E1D00),
    secondaryContainer = Color(0xFF702B00),
    onSecondaryContainer = FasalOrangeContainer,
    background = FasalBackgroundDarkTheme,
    surface = FasalSurfaceDarkTheme,
    surfaceVariant = Color(0xFF2E312C),
    onBackground = Color(0xFFE2E3DD),
    onSurface = Color(0xFFE2E3DD),
    onSurfaceVariant = Color(0xFFC4C8BA),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A)
)

private val LightColorScheme = lightColorScheme(
    primary = FasalGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = FasalGreenContainer,
    onPrimaryContainer = FasalOnGreenContainer,
    secondary = FasalOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = FasalOrangeContainer,
    onSecondaryContainer = FasalOnOrangeContainer,
    background = FasalBackground,
    surface = FasalSurface,
    surfaceVariant = FasalSurfaceVariant,
    onBackground = FasalOnSurface,
    onSurface = FasalOnSurface,
    onSurfaceVariant = FasalOnSurfaceVariant,
    outline = FasalOutline,
    error = FasalError,
    errorContainer = FasalErrorContainer
)

@Composable
fun FasalNetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand agricultural identity consistent
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

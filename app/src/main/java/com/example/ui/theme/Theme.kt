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
    primary = CaliNeonLime,
    onPrimary = Color(0xFF0F1115),
    primaryContainer = Color(0xFF263300),
    onPrimaryContainer = CaliNeonLime,
    secondary = CaliElectricCyan,
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = CaliAmber,
    background = CaliBlack,
    onBackground = CaliTextPrimary,
    surface = CaliSurfaceDark,
    onSurface = CaliTextPrimary,
    surfaceVariant = CaliSurfaceCard,
    onSurfaceVariant = CaliTextSecondary,
    outline = CaliBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4D7C0F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFECFCCB),
    onPrimaryContainer = Color(0xFF1A2E05),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    tertiary = Color(0xFFD97706),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun CaliFormTheme(
    darkTheme: Boolean = true, // Default to sleek athletic dark theme
    dynamicColor: Boolean = false, // Keep branded high contrast theme
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

// Backward compatibility alias for preview
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    CaliFormTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}

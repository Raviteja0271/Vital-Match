package com.simats.vitalmatch.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object ThemeState {
    var themeMode = mutableStateOf("Light")
    var languageMode = mutableStateOf("English")
}

// Ultra High Contrast Dark Mode (Black background, pure white text)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFEF4444),         // Vibrant Red
    secondary = Color(0xFF6366F1),       // Indigo
    tertiary = Color(0xFF10B981),        // Emerald Green
    background = Color(0xFF121212),      // Deep Pure Black Background
    surface = Color(0xFF1E1E1E),         // Dark Elevated Card Surface
    surfaceVariant = Color(0xFF2D2D2D),  // Dark Text Field & Input Surface
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFFFFFFF),    // Bright Pure White Text
    onSurface = Color(0xFFFFFFFF),       // Bright Pure White Card Text
    onSurfaceVariant = Color(0xFFD1D5DB) // Light White Subtext
)

// Clean Crisp Light Mode
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE63946),         // Vibrant Red
    secondary = Color(0xFF4F46E5),       // Indigo
    tertiary = Color(0xFF059669),        // Emerald Green
    background = Color(0xFFF8FAFC),      // Soft Light Background
    surface = Color.White,               // Pure White Card
    surfaceVariant = Color(0xFFF1F5F9),  // Light Gray Input Surface
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),    // Dark Text
    onSurface = Color(0xFF0F172A),       // Dark Card Text
    onSurfaceVariant = Color(0xFF64748B) // Muted Gray Subtext
)

@Composable
fun VITALMATCHTheme(
    darkTheme: Boolean = run {
        val systemInDark = isSystemInDarkTheme()
        when (ThemeState.themeMode.value) {
            "Dark" -> true
            "Light" -> false
            else -> systemInDark
        }
    },
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
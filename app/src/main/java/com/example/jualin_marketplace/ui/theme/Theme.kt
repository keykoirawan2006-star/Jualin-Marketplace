package com.example.jualin_marketplace.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = BackgroundWhite,
    primaryContainer = LightGreenSecondary,
    onPrimaryContainer = EmeraldPrimaryDark,
    secondary = LightGreenSecondary,
    onSecondary = EmeraldPrimary,
    secondaryContainer = LightGreenSecondary,
    onSecondaryContainer = EmeraldPrimaryDark,
    tertiary = AccentOrange,
    onTertiary = BackgroundWhite,
    background = BackgroundWhite,
    onBackground = TextPrimaryDark,
    surface = SurfaceLightGray,
    onSurface = TextPrimaryDark,
    surfaceVariant = BackgroundWhite,
    onSurfaceVariant = TextSecondaryGray,
    outline = BorderGray
)

@Composable
fun JualinMarketplaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity()
            activity?.window?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

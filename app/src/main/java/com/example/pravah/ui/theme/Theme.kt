package com.example.pravah.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PravahLightColorScheme = lightColorScheme(
    primary = LeafGreen500,
    onPrimary = PureWhite,
    primaryContainer = LeafGreen100,
    onPrimaryContainer = LeafGreen700,

    secondary = OceanBlue500,
    onSecondary = PureWhite,
    secondaryContainer = SkyCyan200,
    onSecondaryContainer = OceanBlue700,

    tertiary = SkyCyan500,
    onTertiary = NavyDeep,
    tertiaryContainer = LimeGreen200,
    onTertiaryContainer = LeafGreen700,

    background = OffWhite,
    onBackground = SlateGray700,

    surface = PureWhite,
    onSurface = SlateGray700,
    surfaceVariant = CloudGray100,
    onSurfaceVariant = SlateGray500,

    outline = CloudGray100,
    outlineVariant = SlateGray500,

    error = ErrorRed500,
    onError = PureWhite,
    errorContainer = ErrorRed100,
    onErrorContainer = ErrorRed500,
)

data class PravahExtendedColors(
    val solarBlue: androidx.compose.ui.graphics.Color,   // Icon tint for solar/grid-specific features, distinct data-viz series color
    val limeGreen: androidx.compose.ui.graphics.Color,    // Secondary progress-bar fill, gradient stop, "trending up" indicators
    val warningAmber: androidx.compose.ui.graphics.Color, // Non-critical warning banners/snackbars
    val heroGradient: Brush,
)

private val LocalPravahExtendedColors = staticCompositionLocalOf {
    PravahExtendedColors(
        solarBlue = SolarBlue700,
        limeGreen = LimeGreen500,
        warningAmber = WarningAmber500,
        heroGradient = Brush.linearGradient(listOf(NavyDeep, OceanBlue500, SkyCyan500, LeafGreen500, LimeGreen500)),
    )
}

@Composable
fun PravahAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = PravahLightColorScheme

    val extendedColors = PravahExtendedColors(
        solarBlue = SolarBlue700,
        limeGreen = LimeGreen500,
        warningAmber = WarningAmber500,
        heroGradient = Brush.linearGradient(
            colors = listOf(
                NavyDeep,
                OceanBlue500,
                SkyCyan500,
                LeafGreen500,
                LimeGreen500
            ),
            start = Offset(0f, 0f),
            end = Offset.Infinite,
        ),
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()

            // Always use dark icons because the app is always light themed
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
        }
    }

    CompositionLocalProvider(LocalPravahExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PravahTypography,
            content = content
        )
    }
}
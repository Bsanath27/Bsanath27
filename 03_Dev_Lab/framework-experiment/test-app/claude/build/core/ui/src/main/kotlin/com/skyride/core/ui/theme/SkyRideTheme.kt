package com.skyrik.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Skyrik MaterialTheme wrapper.
 *
 * Provides:
 *  - [MaterialTheme.colorScheme] — mapped from [ColorPalette]
 *  - [SkyrikTheme.spacing]     — design token spacing values
 *  - [SkyrikTheme.shapes]      — design token shape values
 *  - [SkyrikTheme.elevation]   — design token elevation values
 *  - [SkyrikTheme.extendedColors] — semantic colors not in Material3 spec
 *
 * Dynamic color (Material You) is intentionally disabled to preserve the
 * premium branded dark luxury aesthetic on all devices.
 */
@Composable
fun SkyrikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) SkyrikDarkColorScheme else SkyrikLightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalSkyrikSpacing provides SkyrikSpacing(),
        LocalSkyrikShapes provides SkyrikShapes(),
        LocalSkyrikElevation provides SkyrikElevation(),
        LocalSkyrikExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SkyrikTypography,
            content = content,
        )
    }
}

/**
 * Convenience object to access Skyrik-specific design tokens
 * from within the composition tree, mirroring the [MaterialTheme] pattern.
 *
 * Usage: `SkyrikTheme.spacing.space_base`
 */
object SkyrikTheme {

    val spacing: SkyrikSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSkyrikSpacing.current

    val shapes: SkyrikShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalSkyrikShapes.current

    val elevation: SkyrikElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalSkyrikElevation.current

    val extendedColors: SkyrikExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSkyrikExtendedColors.current
}

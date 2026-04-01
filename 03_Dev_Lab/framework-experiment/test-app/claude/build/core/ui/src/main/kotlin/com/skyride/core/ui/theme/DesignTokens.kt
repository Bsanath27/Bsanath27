package com.skyrik.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Skyrik Design Tokens — spacing, shape, and elevation constants.
 * Token names mirror the designer spec exactly.
 *
 * Access via [LocalSkyrikSpacing] and [LocalSkyrikShapes] composition locals,
 * or through [SkyrikTheme.spacing] / [SkyrikTheme.shapes] convenience accessors.
 */

// ─── Spacing tokens ───────────────────────────────────────────────────────────

data class SkyrikSpacing(
    val space_xxs: Dp = 2.dp,
    val space_xs: Dp  = 4.dp,
    val space_sm: Dp  = 8.dp,
    val space_md: Dp  = 12.dp,
    val space_base: Dp = 16.dp,
    val space_lg: Dp  = 20.dp,
    val space_xl: Dp  = 24.dp,
    val space_2xl: Dp = 32.dp,
    val space_3xl: Dp = 48.dp,
    val space_4xl: Dp = 64.dp,
)

// ─── Shape tokens ─────────────────────────────────────────────────────────────

data class SkyrikShapes(
    val shapeSmall: RoundedCornerShape  = RoundedCornerShape(8.dp),
    val shapeMedium: RoundedCornerShape = RoundedCornerShape(16.dp),
    val shapeLarge: RoundedCornerShape  = RoundedCornerShape(24.dp),
    val shapeCircle: RoundedCornerShape = RoundedCornerShape(50),
    val shapeTopRounded: RoundedCornerShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
)

// ─── Elevation tokens ─────────────────────────────────────────────────────────

data class SkyrikElevation(
    val level0: Dp = 0.dp,
    val level1: Dp = 1.dp,
    val level2: Dp = 3.dp,
    val level3: Dp = 6.dp,
    val level4: Dp = 8.dp,
    val level5: Dp = 12.dp,
)

// ─── Composition locals ───────────────────────────────────────────────────────

val LocalSkyrikSpacing = staticCompositionLocalOf { SkyrikSpacing() }
val LocalSkyrikShapes  = staticCompositionLocalOf { SkyrikShapes() }
val LocalSkyrikElevation = staticCompositionLocalOf { SkyrikElevation() }
val LocalSkyrikExtendedColors = staticCompositionLocalOf { DarkExtendedColors }

package com.skyrik.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Skyrik Color Palette — ALL color hex values are defined exclusively in this file.
 * No color literals are permitted anywhere else in the codebase.
 *
 * Palette concept: Dark luxury — deep midnight navy base with gold accents,
 * evoking premium helicopter travel at dusk.
 */

// ─── Raw color constants ──────────────────────────────────────────────────────

// Midnight Navy family
val Navy950 = Color(0xFF050B18)
val Navy900 = Color(0xFF0A1628)
val Navy800 = Color(0xFF0F2040)
val Navy700 = Color(0xFF162D58)
val Navy600 = Color(0xFF1E3A6E)
val Navy500 = Color(0xFF274A8A)

// Gold accent family
val Gold400 = Color(0xFFFFD166)
val Gold500 = Color(0xFFFFC233)
val Gold600 = Color(0xFFE5AA00)
val Gold700 = Color(0xFFB88500)
val GoldContainer = Color(0xFF3D2E00)

// Sky Blue family (secondary)
val SkyBlue300 = Color(0xFF7EC8E3)
val SkyBlue400 = Color(0xFF4BAED1)
val SkyBlue500 = Color(0xFF1F8FB5)
val SkyBlueContainer = Color(0xFF00354D)

// Tertiary — Soft Violet (twilight)
val Violet300 = Color(0xFFB39DDB)
val Violet400 = Color(0xFF9575CD)
val VioletContainer = Color(0xFF2A1A4A)

// Surface family
val Surface50  = Color(0xFF101828)
val Surface100 = Color(0xFF182035)
val Surface200 = Color(0xFF1E2A42)
val Surface300 = Color(0xFF263350)

// On-surface / text
val OnSurface90 = Color(0xFFECF0FF)
val OnSurface70 = Color(0xFFB0BAD0)
val OnSurface50 = Color(0xFF7A8BA8)
val OnSurface30 = Color(0xFF4A5A72)

// Outline
val Outline60 = Color(0xFF3A4B63)
val Outline40 = Color(0xFF263348)

// State colors
val StateSuccess = Color(0xFF4CAF78)
val StateError   = Color(0xFFEF5350)
val StateWarning = Color(0xFFFFA726)
val StateLoading = Color(0xFF4BAED1)

// Live tracking accent
val LiveTracking = Color(0xFF00E676)

// Scrim
val Scrim = Color(0xCC050B18)

// Pure extremes
val PureWhite = Color(0xFFFFFFFF)
val PureBlack = Color(0xFF000000)

// ─── Dark Color Scheme (primary experience) ───────────────────────────────────

val SkyrikDarkColorScheme = darkColorScheme(
    primary                = Gold500,
    onPrimary              = Navy900,
    primaryContainer       = GoldContainer,
    onPrimaryContainer     = Gold400,

    secondary              = SkyBlue400,
    onSecondary            = Navy900,
    secondaryContainer     = SkyBlueContainer,
    onSecondaryContainer   = SkyBlue300,

    tertiary               = Violet400,
    onTertiary             = Navy900,
    tertiaryContainer      = VioletContainer,
    onTertiaryContainer    = Violet300,

    background             = Navy950,
    onBackground           = OnSurface90,

    surface                = Surface50,
    onSurface              = OnSurface90,
    surfaceVariant         = Surface200,
    onSurfaceVariant       = OnSurface70,

    outline                = Outline60,
    outlineVariant         = Outline40,

    error                  = StateError,
    onError                = PureWhite,
    errorContainer         = Color(0xFF4B1212),
    onErrorContainer       = Color(0xFFFFB4AB),

    inverseSurface         = OnSurface90,
    inverseOnSurface       = Navy900,

    scrim                  = Scrim,
)

// ─── Light Color Scheme (accessibility / daytime variant) ────────────────────

val SkyrikLightColorScheme = lightColorScheme(
    primary                = Gold700,
    onPrimary              = PureWhite,
    primaryContainer       = Color(0xFFFFF0C0),
    onPrimaryContainer     = Color(0xFF3D2E00),

    secondary              = SkyBlue500,
    onSecondary            = PureWhite,
    secondaryContainer     = Color(0xFFCCEEF8),
    onSecondaryContainer   = Color(0xFF00354D),

    tertiary               = Violet400,
    onTertiary             = PureWhite,
    tertiaryContainer      = Color(0xFFEDE7F6),
    onTertiaryContainer    = Color(0xFF2A1A4A),

    background             = Color(0xFFF5F7FF),
    onBackground           = Navy900,

    surface                = PureWhite,
    onSurface              = Navy900,
    surfaceVariant         = Color(0xFFE8EDF5),
    onSurfaceVariant       = Navy700,

    outline                = Color(0xFF8A9BBF),
    outlineVariant         = Color(0xFFCDD5E5),

    error                  = Color(0xFFB00020),
    onError                = PureWhite,
    errorContainer         = Color(0xFFFFDAD6),
    onErrorContainer       = Color(0xFF410002),

    inverseSurface         = Navy800,
    inverseOnSurface       = OnSurface90,

    scrim                  = Color(0x99000000),
)

// ─── Semantic extension colors (not in Material3 scheme) ─────────────────────
// Access via LocalSkyrikExtendedColors composition local (see SkyrikTheme.kt)

data class SkyrikExtendedColors(
    val colorStateSelected: Color,
    val colorStateDisabled: Color,
    val colorStateLoading: Color,
    val colorStateSuccess: Color,
    val colorStateWarning: Color,
    val colorLiveTracking: Color,
)

val DarkExtendedColors = SkyrikExtendedColors(
    colorStateSelected  = Gold500,
    colorStateDisabled  = OnSurface30,
    colorStateLoading   = StateLoading,
    colorStateSuccess   = StateSuccess,
    colorStateWarning   = StateWarning,
    colorLiveTracking   = LiveTracking,
)

val LightExtendedColors = SkyrikExtendedColors(
    colorStateSelected  = Gold700,
    colorStateDisabled  = Color(0xFFBDBDBD),
    colorStateLoading   = SkyBlue500,
    colorStateSuccess   = Color(0xFF2E7D32),
    colorStateWarning   = Color(0xFFE65100),
    colorLiveTracking   = Color(0xFF00C853),
)

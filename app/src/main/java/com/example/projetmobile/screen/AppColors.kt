package com.example.projetmobile.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Palette de couleurs ───────────────────────────────────────────────────────

data class AppColorScheme(
    val Background: Color,
    val SurfaceCard: Color,
    val Primary: Color,
    val PrimaryLight: Color,
    val Secondary: Color,
    val SecondaryLight: Color,
    val Error: Color,
    val ErrorLight: Color,
    val TextPrimary: Color,
    val TextSecondary: Color,
    val TextHint: Color,
    val Divider: Color,
    val Gold: Color,
    val Silver: Color,
    val Bronze: Color
)

val LightColors = AppColorScheme(
    Background    = Color(0xFFF5F7FF),
    SurfaceCard   = Color(0xFFFFFFFF),
    Primary       = Color(0xFF5B5FEF),
    PrimaryLight  = Color(0xFFEEEFFF),
    Secondary     = Color(0xFF10B981),
    SecondaryLight= Color(0xFFD1FAE5),
    Error         = Color(0xFFEF4444),
    ErrorLight    = Color(0xFFFFE4E4),
    TextPrimary   = Color(0xFF1E1B4B),
    TextSecondary = Color(0xFF6B7280),
    TextHint      = Color(0xFF9CA3AF),
    Divider       = Color(0xFFE5E7EB),
    Gold          = Color(0xFFF59E0B),
    Silver        = Color(0xFF94A3B8),
    Bronze        = Color(0xFFCD7F32)
)

val DarkColors = AppColorScheme(
    Background    = Color(0xFF0F0E1A),
    SurfaceCard   = Color(0xFF1C1B2E),
    Primary       = Color(0xFF8B8FFF),
    PrimaryLight  = Color(0xFF2D2B4E),
    Secondary     = Color(0xFF34D399),
    SecondaryLight= Color(0xFF0D3D2E),
    Error         = Color(0xFFFC8181),
    ErrorLight    = Color(0xFF3D1515),
    TextPrimary   = Color(0xFFEEEEFF),
    TextSecondary = Color(0xFFB0B7D3),
    TextHint      = Color(0xFF6B7280),
    Divider       = Color(0xFF2D2B4E),
    Gold          = Color(0xFFFBBF24),
    Silver        = Color(0xFF94A3B8),
    Bronze        = Color(0xFFCD7F32)
)

// CompositionLocal — fourni par ProjetMobileApp
val LocalIsDark = compositionLocalOf { false }

// Propriété composable : AppColors.Primary fonctionne tel quel dans tous les écrans
val AppColors: AppColorScheme
    @Composable
    get() = if (LocalIsDark.current) DarkColors else LightColors

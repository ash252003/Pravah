package com.example.pravah.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PravahDisplayFont: FontFamily = FontFamily.Default // swap for Poppins — headlines, big stats, app name on splash
val PravahBodyFont: FontFamily = FontFamily.Default     // swap for Inter — body copy, labels, buttons, dense data lists

val PravahTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle( // Secondary hero numbers, onboarding headline text
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    displaySmall = TextStyle( // Empty-state headline ("No devices connected yet")
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),

    headlineLarge = TextStyle(
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle( // Section headers within a scrollable screen ("This Week", "Devices")
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle( // Card titles for large feature cards (e.g. "AI Recommendation")
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),

    // TopAppBar title text — what the user sees at the very top of every screen.
    titleLarge = TextStyle(
        fontFamily = PravahDisplayFont,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle( // ListItem headline text, dialog titles, NavigationDrawer item labels
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle( // Chip labels, small card titles, TabRow tab text
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle( // Secondary/supporting text under a headline, ListItem supporting text
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle( // Timestamps, fine print, helper/error text under TextFields
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),

    // Button() label text — pairs with `primary`/`secondary` colors from Theme.kt.
    labelLarge = TextStyle(
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle( // NavigationBar item labels, small button text
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle( // Overline-style tags, Badge() numbers, smallest UI text
        fontFamily = PravahBodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
package com.example.pravah.ui.theme

import androidx.compose.ui.graphics.Color

val NavyDeep = Color(0xFF0A1929) // Darkest navy — logo background (dark variant), app dark-theme background, high-emphasis text on light surfaces
val NavyMedium = Color(0xFF13294B) // Slightly lighter navy — dark-theme surfaces/cards, top app bar in dark mode
val OceanBlue700 = Color(0xFF115E93) // Deep swirl blue — pressed/active states for blue elements
val OceanBlue500 = Color(0xFF1B7CB8) // Core swirl blue — SECONDARY brand color: secondary buttons, links, selected tab indicator
val SkyCyan500 = Color(0xFF35C4E0) // Bright cyan tip of swirl — TERTIARY brand color: highlights, badges, chart accents, AI-chip glow
val SkyCyan200 = Color(0xFFB7EAF3) // Pale cyan tint — chip/container backgrounds, subtle hover states
val SolarBlue700 = Color(0xFF17418C) // Deep solar-panel blue — used sparingly for data-viz (distinct from Ocean/Sky so charts stay legible), icon tint for "grid/solar" features

val LeafGreen700 = Color(0xFF1E7A38) // Deep leaf green — pressed state for primary buttons
val LeafGreen500 = Color(0xFF2FA84F) // Core leaf green — PRIMARY brand color: main CTA buttons, FAB, active toggles/switches
val LeafGreen100 = Color(0xFFD7F2DC) // Pale green tint — primaryContainer, success-state backgrounds, chip fills
val LimeGreen500 = Color(0xFFA4D65E) // Lime accent from wordmark gradient tail — secondary accents, progress bar "fill" endpoints, gradient stop
val LimeGreen200 = Color(0xFFE3F3C7) // Very pale lime — subtle highlight backgrounds, empty-state illustrations

val PureWhite = Color(0xFFFFFFFF) // Logo's light background / wordmark on dark bg — light-theme background & surfaces, dark-theme text
val OffWhite = Color(0xFFF5F8FA) // Very light cool-gray — light-theme "background" (softer than pure white, reduces glare)
val CloudGray100 = Color(0xFFE7ECF0) // Card borders, dividers, disabled backgrounds (light theme)
val SlateGray500 = Color(0xFF5B6B79) // Secondary/muted body text, icons (light theme), placeholder text
val SlateGray700 = Color(0xFF33414D) // Primary body text on light surfaces (softer than pure navy for long-form reading)
val CharcoalSurface = Color(0xFF152238) // Dark-theme elevated surface (cards, sheets, dialogs) — one step up from NavyDeep

val ErrorRed500 = Color(0xFFDC3545) // Destructive actions, form validation errors, "energy waste/alert" states
val ErrorRed100 = Color(0xFFFBE1E3) // errorContainer background
val WarningAmber500 = Color(0xFFF2A93B) // Non-critical warnings (e.g. "near budget limit"), pairs with the warm tone missing from the logo so it stands out intentionally
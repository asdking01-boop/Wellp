package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ArabicWallsDarkColorScheme = darkColorScheme(
  primary = GoldPrimary,
  onPrimary = DarkBackground,
  primaryContainer = GoldContainer,
  onPrimaryContainer = OnGoldContainer,
  secondary = GoldMetallic,
  onSecondary = DarkBackground,
  secondaryContainer = DarkSurfaceElevated,
  onSecondaryContainer = GoldLight,
  tertiary = GoldLight,
  onTertiary = DarkBackground,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
  outlineVariant = DarkGoldBorder
)

val ArabicWallsShapes = Shapes(
  small = RoundedCornerShape(10.dp),
  medium = RoundedCornerShape(18.dp),
  large = RoundedCornerShape(24.dp),
  extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun ArabicWallsTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = ArabicWallsDarkColorScheme,
    typography = Typography,
    shapes = ArabicWallsShapes,
    content = content
  )
}

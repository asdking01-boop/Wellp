package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkGoldBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.OnGoldContainer
import com.example.ui.theme.PremiumBadgeBg
import com.example.ui.theme.PremiumBadgeText
import com.example.viewmodel.Screen

@Composable
fun ArabicWallsTopBar(
  language: AppLanguage,
  isPremium: Boolean,
  onToggleLanguage: () -> Unit,
  onOpenPremium: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = DarkBackground.copy(alpha = 0.95f),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Brand Title with Gold crescent accent
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.testTag("app_brand_header")
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(GoldLight, GoldPrimary)))
            .padding(2.dp),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(DarkBackground),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "☪",
              color = GoldPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = if (language == AppLanguage.ARABIC) "خلفيات عربية" else "ArabicWalls",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
          Text(
            text = if (language == AppLanguage.ARABIC) "فن إسلامي وخزائن خط 4K" else "Islamic Art & Calligraphy 4K",
            fontFamily = CairoFontFamily,
            fontSize = 10.sp,
            color = GoldMetallic
          )
        }
      }

      // Actions: Language Toggle & Premium Crown
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Language Toggle Pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onToggleLanguage)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("language_toggle_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = "Language",
              tint = GoldPrimary,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = if (language == AppLanguage.ARABIC) "EN" else "عربي",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              color = Color.White
            )
          }
        }

        // Premium Crown
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              if (isPremium) Brush.horizontalGradient(listOf(Color(0xFF0D9488), Color(0xFF10B981)))
              else Brush.horizontalGradient(listOf(GoldPrimary, GoldLight))
            )
            .clickable(onClick = onOpenPremium)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("premium_header_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = if (isPremium) Icons.Default.Check else Icons.Default.AutoAwesome,
              contentDescription = "Premium",
              tint = if (isPremium) Color.White else Color(0xFF1C1300),
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = if (isPremium) {
                if (language == AppLanguage.ARABIC) "مُفَعّل" else "PRO"
              } else {
                if (language == AppLanguage.ARABIC) "ترقية" else "VIP"
              },
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = if (isPremium) Color.White else Color(0xFF1C1300)
            )
          }
        }
      }
    }
  }
}

@Composable
fun ArabicWallsBottomNav(
  currentScreen: Screen,
  onNavigate: (Screen) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = DarkSurface,
    contentColor = Color.White,
    tonalElevation = 8.dp,
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, DarkBorder)
  ) {
    val items = listOf(
      Triple(Screen.HOME, Strings.get("home"), Icons.Default.Home),
      Triple(Screen.FAVORITES, Strings.get("favorites"), Icons.Default.Bookmark),
      Triple(Screen.QUOTE_EDITOR, Strings.get("quote_editor"), Icons.Default.FormatQuote),
      Triple(Screen.ADMIN_BULK_GENERATOR, Strings.get("admin_generator"), Icons.Default.Wallpaper),
      Triple(Screen.PREMIUM, Strings.get("premium"), Icons.Default.AutoAwesome)
    )

    items.forEach { (screen, label, icon) ->
      val selected = currentScreen == screen
      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(screen) },
        icon = {
          Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = label,
            fontFamily = CairoFontFamily,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = DarkBackground,
          selectedTextColor = GoldLight,
          indicatorColor = GoldPrimary,
          unselectedIconColor = Color(0xFF7E8B9B),
          unselectedTextColor = Color(0xFF7E8B9B)
        ),
        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
      )
    }
  }
}

@Composable
fun SetWallpaperTargetDialog(
  onDismiss: () -> Unit,
  onSelectTarget: (Int) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = DarkSurfaceElevated,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Wallpaper,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = Strings.get("choose_wallpaper_target"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = Color.White
        )
      }
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Button(
          onClick = { onSelectTarget(1) },
          colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("target_home_screen_btn")
        ) {
          Text(
            text = Strings.get("target_home"),
            fontFamily = CairoFontFamily,
            color = Color.White
          )
        }
        Button(
          onClick = { onSelectTarget(2) },
          colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("target_lock_screen_btn")
        ) {
          Text(
            text = Strings.get("target_lock"),
            fontFamily = CairoFontFamily,
            color = Color.White
          )
        }
        Button(
          onClick = { onSelectTarget(3) },
          colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("target_both_screens_btn")
        ) {
          Text(
            text = Strings.get("target_both"),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            color = DarkBackground
          )
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(
          text = Strings.get("cancel"),
          fontFamily = CairoFontFamily,
          color = Color.White.copy(alpha = 0.7f)
        )
      }
    }
  )
}

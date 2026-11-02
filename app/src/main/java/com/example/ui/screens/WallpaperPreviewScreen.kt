package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.Wallpaper
import com.example.ui.components.SetWallpaperTargetDialog
import com.example.ui.components.WallpaperCanvasVisual
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkGoldBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PremiumBadgeBg
import com.example.ui.theme.PremiumBadgeText

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WallpaperPreviewScreen(
  wallpaper: Wallpaper,
  language: AppLanguage,
  isFavorite: Boolean,
  isUserPremium: Boolean,
  isApplying: Boolean,
  showSetDialog: Boolean,
  onToggleFavorite: () -> Unit,
  onShowSetDialog: (Boolean) -> Unit,
  onApplyWallpaper: (Int) -> Unit,
  onDownload: () -> Unit,
  onOpenQuoteEditor: (Wallpaper) -> Unit,
  onOpenPremium: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showChrome by remember { mutableStateOf(true) }
  var showLockScreenMock by remember { mutableStateOf(false) }
  var showInfoSheet by remember { mutableStateOf(false) }

  val isLocked = wallpaper.isPremium && !isUserPremium

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    // 1. Full-Screen Wallpaper Canvas (tap anywhere to toggle UI chrome)
    WallpaperCanvasVisual(
      wallpaper = wallpaper,
      modifier = Modifier
        .fillMaxSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null
        ) {
          showChrome = !showChrome
        }
    )

    // 2. Simulated Lock Screen Clock & Notification Overlay
    if (showLockScreenMock) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = null,
          tint = Color.White.copy(alpha = 0.8f),
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "09:41",
          fontSize = 68.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White,
          fontFamily = CairoFontFamily
        )
        Text(
          text = if (language == AppLanguage.ARABIC) "الأربعاء، ١٤ رمضان ١٤٤٨ هـ" else "Wednesday, Ramadan 14, 1448 AH",
          fontSize = 15.sp,
          color = GoldLight,
          fontFamily = CairoFontFamily
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Lock screen prayer / quote widget pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x55000000))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Text(
            text = if (language == AppLanguage.ARABIC) "صلاة العصر: ٣:٤٥ م • الرياض" else "Asr Prayer: 3:45 PM • Makkah",
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = Color.White
          )
        }
      }
    }

    // 3. Top Floating App Bar
    AnimatedVisibility(
      visible = showChrome,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color(0xCC090D14), Color.Transparent)
            )
          )
          .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Back Button
        IconButton(
          onClick = onBack,
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
            .testTag("preview_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }

        // Action controls (Lockscreen toggle, Info)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Lock Screen Preview Toggle
          IconButton(
            onClick = { showLockScreenMock = !showLockScreenMock },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (showLockScreenMock) GoldPrimary else Color(0x66000000))
              .testTag("toggle_lockscreen_mock_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = Strings.get("preview_lockscreen"),
              tint = if (showLockScreenMock) DarkBackground else Color.White
            )
          }

          // Info Button
          IconButton(
            onClick = { showInfoSheet = true },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0x66000000))
              .testTag("wallpaper_info_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Info",
              tint = Color.White
            )
          }
        }
      }
    }

    // 4. Bottom Action Bar with Buttons: Set as Wallpaper, Download, Favorite, Quote Editor
    AnimatedVisibility(
      visible = showChrome,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color(0xEE090D14), DarkBackground)
            )
          )
          .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // If wallpaper is premium and user is locked
        if (isLocked) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(Brush.horizontalGradient(listOf(Color(0xFF281F0D), Color(0xFF1A1408))))
              .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
              .clickable(onClick = onOpenPremium)
              .padding(14.dp)
              .testTag("locked_wallpaper_banner")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = null,
                  tint = GoldLight,
                  modifier = Modifier.size(24.dp)
                )
                Column {
                  Text(
                    text = if (language == AppLanguage.ARABIC) "خلفية مميزة - النسخة الذهبية" else "Premium Wallpaper - Gold Edition",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = GoldLight
                  )
                  Text(
                    text = if (language == AppLanguage.ARABIC) "انقر للترقية وفتح جميع الخلفيات 4K" else "Tap to unlock all 4K wallpapers",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    color = Color.LightGray
                  )
                }
              }
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = GoldPrimary
              )
            }
          }
        }

        // Wallpaper Title & Subtitle in preview
        Column {
          Text(
            text = if (language == AppLanguage.ARABIC) wallpaper.titleAr else wallpaper.title,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
          Text(
            text = if (language == AppLanguage.ARABIC) wallpaper.subtextArabic else wallpaper.subtextEnglish,
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = GoldMetallic
          )
        }

        // Primary Row of Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 1. Set as Wallpaper Button (Main Action)
          Button(
            onClick = {
              if (isLocked) onOpenPremium() else onShowSetDialog(true)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isLocked) DarkSurfaceElevated else GoldPrimary
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .weight(1.4f)
              .height(52.dp)
              .testTag("set_wallpaper_button")
          ) {
            if (isApplying) {
              CircularProgressIndicator(
                color = DarkBackground,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
              )
            } else {
              Icon(
                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.Wallpaper,
                contentDescription = null,
                tint = if (isLocked) GoldLight else DarkBackground,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = Strings.get("set_as_wallpaper"),
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isLocked) GoldLight else DarkBackground
              )
            }
          }

          // 2. Download Button
          IconButton(
            onClick = {
              if (isLocked) onOpenPremium() else onDownload()
            },
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(DarkSurfaceElevated)
              .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
              .testTag("download_wallpaper_button")
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = Strings.get("download"),
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          // 3. Favorite Button
          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(DarkSurfaceElevated)
              .border(
                width = 1.dp,
                color = if (isFavorite) DarkGoldBorder else DarkBorder,
                shape = RoundedCornerShape(16.dp)
              )
              .testTag("favorite_toggle_button")
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = Strings.get("favorite"),
              tint = if (isFavorite) GoldPrimary else Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          // 4. Quote Editor Button (Customize with text)
          IconButton(
            onClick = { onOpenQuoteEditor(wallpaper) },
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(DarkSurfaceElevated)
              .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
              .testTag("open_quote_editor_button")
          ) {
            Icon(
              imageVector = Icons.Default.FormatQuote,
              contentDescription = Strings.get("edit_quote"),
              tint = GoldLight,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }

    // Set Wallpaper Dialog
    if (showSetDialog) {
      SetWallpaperTargetDialog(
        onDismiss = { onShowSetDialog(false) },
        onSelectTarget = onApplyWallpaper
      )
    }

    // Info Bottom Sheet
    if (showInfoSheet) {
      ModalBottomSheet(
        onDismissRequest = { showInfoSheet = false },
        containerColor = DarkSurfaceElevated
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = Strings.get("wallpaper_info"),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = Strings.get("resolution_4k"),
              fontFamily = CairoFontFamily,
              color = GoldMetallic,
              fontSize = 13.sp
            )
            Text(
              text = "${wallpaper.downloads} ${Strings.get("downloads_count")}",
              fontFamily = CairoFontFamily,
              color = Color.LightGray,
              fontSize = 13.sp
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = Strings.get("artist") + ": " + wallpaper.artist,
              fontFamily = CairoFontFamily,
              color = Color.White,
              fontSize = 13.sp
            )
          }

          // Tags
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            wallpaper.tags.forEach { tag ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(DarkSurface)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "#$tag",
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  color = GoldLight
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WallpaperData
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.QuotePreset
import com.example.ui.components.WallpaperCanvasVisual
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.viewmodel.QuoteEditorState

@Composable
fun QuoteEditorScreen(
  state: QuoteEditorState,
  language: AppLanguage,
  onUpdateText: (String) -> Unit,
  onUpdateSource: (String) -> Unit,
  onSelectPreset: (QuotePreset) -> Unit,
  onToggleFont: (Boolean) -> Unit,
  onUpdateFontSize: (Float) -> Unit,
  onSelectTextColor: (Int) -> Unit,
  onSelectPosition: (Int) -> Unit,
  onUpdateDimOverlay: (Float) -> Unit,
  onApplyAsWallpaper: () -> Unit,
  onDownloadArtwork: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
      .verticalScroll(scrollState)
      .padding(bottom = 90.dp)
  ) {
    // 1. Live Preview of Edited Wallpaper
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .aspectRatio(0.85f)
        .clip(RoundedCornerShape(24.dp))
        .background(DarkSurfaceElevated)
        .border(1.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
        .shadow(12.dp, RoundedCornerShape(24.dp))
        .testTag("quote_preview_card")
    ) {
      // Background Wallpaper with Dim Blur Overlay
      WallpaperCanvasVisual(
        wallpaper = state.wallpaper,
        dimOverlay = state.dimBlurOverlay,
        modifier = Modifier.fillMaxSize()
      )

      // Overlaid Quote Content
      val contentAlignment = when (state.positionIndex) {
        0 -> Alignment.TopCenter
        2 -> Alignment.BottomCenter
        else -> Alignment.Center
      }

      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(
            horizontal = 24.dp,
            vertical = if (state.positionIndex == 1) 16.dp else 40.dp
          ),
        contentAlignment = contentAlignment
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Quote Decorative Mark
          Text(
            text = "❝",
            color = Color(state.textColorHex).copy(alpha = 0.6f),
            fontSize = 24.sp
          )

          // Main Quote Text
          Text(
            text = state.text.ifBlank { Strings.get("enter_custom_text") },
            fontFamily = if (state.isCairoFont) CairoFontFamily else AmiriFontFamily,
            fontSize = state.fontSizeSp.sp,
            lineHeight = (state.fontSizeSp * 1.4f).sp,
            color = Color(state.textColorHex),
            textAlign = TextAlign.Center,
            fontWeight = if (state.isCairoFont) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          // Source Citation Subtext
          if (state.source.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "— ${state.source} —",
              fontFamily = if (state.isCairoFont) CairoFontFamily else AmiriFontFamily,
              fontSize = (state.fontSizeSp * 0.55f).sp,
              color = Color(state.textColorHex).copy(alpha = 0.8f),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }

    // 2. Action Buttons: Apply as Wallpaper & Download
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = onApplyAsWallpaper,
        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("quote_apply_wallpaper_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Wallpaper,
          contentDescription = null,
          tint = DarkBackground,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = Strings.get("apply_as_wallpaper"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = DarkBackground
        )
      }

      OutlinedButton(
        onClick = onDownloadArtwork,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(GoldPrimary, GoldLight))),
        modifier = Modifier
          .weight(0.9f)
          .height(48.dp)
          .testTag("quote_download_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Download,
          contentDescription = null,
          tint = GoldLight,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = Strings.get("save_artwork"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = GoldLight
        )
      }
    }

    // 3. Preset Quotes Carousel (Quran, Hadith)
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp)
    ) {
      Text(
        text = Strings.get("preset_quotes"),
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        WallpaperData.curatedQuotePresets.forEach { preset ->
          val isSelected = state.text == preset.arabicText || state.text == preset.englishText
          Box(
            modifier = Modifier
              .width(220.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(if (isSelected) DarkSurfaceElevated else DarkSurface)
              .border(
                width = 1.dp,
                color = if (isSelected) GoldPrimary else DarkBorder,
                shape = RoundedCornerShape(16.dp)
              )
              .clickable { onSelectPreset(preset) }
              .padding(12.dp)
              .testTag("preset_quote_${preset.id}")
          ) {
            Column {
              Text(
                text = if (language == AppLanguage.ARABIC) preset.arabicText else preset.englishText,
                fontFamily = AmiriFontFamily,
                fontSize = 13.sp,
                color = if (isSelected) GoldLight else Color.White,
                maxLines = 2
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (language == AppLanguage.ARABIC) preset.sourceAr else preset.sourceEn,
                fontFamily = CairoFontFamily,
                fontSize = 10.sp,
                color = GoldMetallic
              )
            }
          }
        }
      }
    }

    // 4. Custom Text Inputs
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedTextField(
        value = state.text,
        onValueChange = onUpdateText,
        label = {
          Text(
            text = Strings.get("enter_custom_text"),
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = Color(0xFFA0B0C4)
          )
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DarkSurfaceElevated,
          unfocusedContainerColor = DarkSurface,
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = DarkBorder,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        maxLines = 3,
        modifier = Modifier.fillMaxWidth().testTag("custom_quote_input")
      )

      OutlinedTextField(
        value = state.source,
        onValueChange = onUpdateSource,
        label = {
          Text(
            text = Strings.get("source_reference"),
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = Color(0xFFA0B0C4)
          )
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DarkSurfaceElevated,
          unfocusedContainerColor = DarkSurface,
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = DarkBorder,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("custom_quote_source_input")
      )
    }

    // 5. Controls Panel: Font Family, Size, Color, Position, Dim Blur
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Font Family Toggle (Cairo vs Amiri)
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = Strings.get("font_family"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = Color.White
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Amiri Button
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(if (!state.isCairoFont) GoldPrimary else DarkSurfaceElevated)
              .border(1.dp, if (!state.isCairoFont) GoldLight else DarkBorder, RoundedCornerShape(14.dp))
              .clickable { onToggleFont(false) }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = Strings.get("font_amiri"),
              fontFamily = AmiriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = if (!state.isCairoFont) DarkBackground else Color.White
            )
          }

          // Cairo Button
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(if (state.isCairoFont) GoldPrimary else DarkSurfaceElevated)
              .border(1.dp, if (state.isCairoFont) GoldLight else DarkBorder, RoundedCornerShape(14.dp))
              .clickable { onToggleFont(true) }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = Strings.get("font_cairo"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = if (state.isCairoFont) DarkBackground else Color.White
            )
          }
        }
      }

      // Font Size Slider
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = Strings.get("font_size"),
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = Color.White
          )
          Text(
            text = "${state.fontSizeSp.toInt()} sp",
            fontFamily = CairoFontFamily,
            color = GoldLight,
            fontSize = 13.sp
          )
        }
        Slider(
          value = state.fontSizeSp,
          onValueChange = onUpdateFontSize,
          valueRange = 16f..40f,
          colors = SliderDefaults.colors(
            thumbColor = GoldPrimary,
            activeTrackColor = GoldPrimary,
            inactiveTrackColor = DarkBorder
          ),
          modifier = Modifier.testTag("font_size_slider")
        )
      }

      // Text Color Palette Selector
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = Strings.get("text_color"),
          fontFamily = CairoFontFamily,
          fontSize = 13.sp,
          color = Color.White
        )
        val textColors = listOf(
          0xFFF6E27A.toInt() to "Gold",
          0xFFFFFFFF.toInt() to "White",
          0xFFFEF3C7.toInt() to "Ivory",
          0xFF6EE7B7.toInt() to "Emerald",
          0xFF93C5FD.toInt() to "Sapphire",
          0xFFE2E8F0.toInt() to "Silver"
        )
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          textColors.forEach { (colorHex, _) ->
            val isSelected = state.textColorHex == colorHex
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(colorHex))
                .border(
                  width = if (isSelected) 3.dp else 1.dp,
                  color = if (isSelected) Color.White else DarkBorder,
                  shape = CircleShape
                )
                .clickable { onSelectTextColor(colorHex) }
            )
          }
        }
      }

      // Position Controls (Top, Center, Bottom)
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = Strings.get("position"),
          fontFamily = CairoFontFamily,
          fontSize = 13.sp,
          color = Color.White
        )
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          val positions = listOf(
            0 to Strings.get("pos_top"),
            1 to Strings.get("pos_center"),
            2 to Strings.get("pos_bottom")
          )
          positions.forEach { (index, title) ->
            val isSelected = state.positionIndex == index
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) GoldPrimary else DarkSurfaceElevated)
                .border(1.dp, if (isSelected) GoldLight else DarkBorder, RoundedCornerShape(12.dp))
                .clickable { onSelectPosition(index) }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = title,
                fontFamily = CairoFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                color = if (isSelected) DarkBackground else Color.White
              )
            }
          }
        }
      }

      // Background Dim & Blur Overlay Slider
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = Strings.get("bg_dim_blur"),
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = Color.White
          )
          Text(
            text = "${(state.dimBlurOverlay * 100).toInt()}%",
            fontFamily = CairoFontFamily,
            color = GoldLight,
            fontSize = 13.sp
          )
        }
        Slider(
          value = state.dimBlurOverlay,
          onValueChange = onUpdateDimOverlay,
          valueRange = 0f..0.85f,
          colors = SliderDefaults.colors(
            thumbColor = GoldPrimary,
            activeTrackColor = GoldPrimary,
            inactiveTrackColor = DarkBorder
          ),
          modifier = Modifier.testTag("dim_overlay_slider")
        )
      }
    }
  }
}

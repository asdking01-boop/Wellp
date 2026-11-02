package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.Wallpaper
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary

@Composable
fun FavoritesScreen(
  favoriteWallpapers: List<Wallpaper>,
  language: AppLanguage,
  isUserPremium: Boolean,
  onToggleFavorite: (String) -> Unit,
  onOpenPreview: (Wallpaper) -> Unit,
  onExploreWallpapers: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    // Header Title
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Bookmark,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(24.dp)
        )
        Text(
          text = Strings.get("favorites_title"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color.White
        )
      }
    }

    if (favoriteWallpapers.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(RoundedCornerShape(36.dp))
              .background(DarkSurfaceElevated),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bookmark,
              contentDescription = null,
              tint = GoldLight.copy(alpha = 0.5f),
              modifier = Modifier.size(36.dp)
            )
          }

          Text(
            text = Strings.get("no_favorites_yet"),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White,
            textAlign = TextAlign.Center
          )

          Text(
            text = Strings.get("no_favorites_desc"),
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = Color(0xFF9DAEC5),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = onExploreWallpapers,
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("explore_wallpapers_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Explore,
              contentDescription = null,
              tint = DarkBackground,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = Strings.get("explore_wallpapers"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              color = DarkBackground
            )
          }
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
          .fillMaxSize()
          .testTag("favorites_grid")
      ) {
        items(favoriteWallpapers, key = { it.id }) { wallpaper ->
          WallpaperGridCard(
            wallpaper = wallpaper,
            language = language,
            isFavorite = true,
            isUserPremium = isUserPremium,
            onToggleFavorite = { onToggleFavorite(wallpaper.id) },
            onClick = { onOpenPreview(wallpaper) }
          )
        }
      }
    }
  }
}

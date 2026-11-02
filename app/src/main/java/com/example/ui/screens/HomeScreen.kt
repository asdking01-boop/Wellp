package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.ColorPalette
import com.example.model.Wallpaper
import com.example.model.WallpaperCategory
import com.example.ui.components.WallpaperCanvasVisual
import com.example.ui.theme.AmiriFontFamily
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
import com.example.ui.theme.PremiumBadgeBg
import com.example.ui.theme.PremiumBadgeText
import com.example.viewmodel.WallpaperUiState

@Composable
fun HomeScreen(
  uiState: WallpaperUiState,
  onSelectCategory: (WallpaperCategory) -> Unit,
  onSelectColor: (ColorPalette) -> Unit,
  onSearchChange: (String) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onOpenPreview: (Wallpaper) -> Unit,
  onLoadMore: () -> Unit,
  modifier: Modifier = Modifier
) {
  val gridState = rememberLazyGridState()

  // Filter wallpapers by category, color, and search query
  val filteredWallpapers = remember(
    uiState.allWallpapers,
    uiState.selectedCategory,
    uiState.selectedColor,
    uiState.searchQuery
  ) {
    uiState.allWallpapers.filter { wall ->
      val matchesCat = uiState.selectedCategory == WallpaperCategory.ALL || wall.category == uiState.selectedCategory
      val matchesColor = uiState.selectedColor == ColorPalette.ALL || wall.colors.any { it.equals(uiState.selectedColor.hex, ignoreCase = true) }
      val matchesSearch = uiState.searchQuery.isBlank() ||
        wall.title.contains(uiState.searchQuery, ignoreCase = true) ||
        wall.titleAr.contains(uiState.searchQuery, ignoreCase = true) ||
        wall.tags.any { it.contains(uiState.searchQuery, ignoreCase = true) }
      matchesCat && matchesColor && matchesSearch
    }
  }

  // Infinite scroll listener: load more when reaching the end of list
  val shouldLoadMore by remember {
    derivedStateOf {
      val totalItems = gridState.layoutInfo.totalItemsCount
      val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
      totalItems > 0 && lastVisibleItem >= totalItems - 2
    }
  }

  LaunchedEffect(shouldLoadMore) {
    if (shouldLoadMore && filteredWallpapers.size >= uiState.displayedWallpaperCount) {
      onLoadMore()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    // 1. Search Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
      OutlinedTextField(
        value = uiState.searchQuery,
        onValueChange = onSearchChange,
        placeholder = {
          Text(
            text = Strings.get("search_hint"),
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = Color(0xFF6F7E91)
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = GoldPrimary,
            modifier = Modifier.size(20.dp)
          )
        },
        trailingIcon = {
          if (uiState.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = Color.Gray,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DarkSurfaceElevated,
          unfocusedContainerColor = DarkSurface,
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = DarkBorder,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_text_input")
      )
    }

    // 2. Category Chips Bar
    val categories = listOf(
      WallpaperCategory.ALL,
      WallpaperCategory.CALLIGRAPHY,
      WallpaperCategory.ARABIC_LETTERS,
      WallpaperCategory.OCCASIONS,
      WallpaperCategory.RAMADAN,
      WallpaperCategory.EID,
      WallpaperCategory.GEOMETRIC,
      WallpaperCategory.DARK,
      WallpaperCategory.NATURE
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { category ->
        val selected = uiState.selectedCategory == category
        val label = if (uiState.language == AppLanguage.ARABIC) category.titleAr else category.titleEn

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              if (selected) Brush.horizontalGradient(listOf(GoldPrimary, GoldLight))
              else Brush.horizontalGradient(listOf(DarkSurfaceElevated, DarkSurfaceVariant))
            )
            .border(
              width = if (selected) 1.5.dp else 1.dp,
              color = if (selected) GoldLight else DarkBorder,
              shape = RoundedCornerShape(20.dp)
            )
            .clickable { onSelectCategory(category) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("category_chip_${category.id}"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = label,
            fontFamily = CairoFontFamily,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp,
            color = if (selected) DarkBackground else Color.White
          )
        }
      }
    }

    // 3. Color Filter Palette Dots
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = Strings.get("filter_colors") + ":",
        fontFamily = CairoFontFamily,
        fontSize = 11.sp,
        color = Color(0xFF8898AA)
      )

      ColorPalette.values().forEach { palette ->
        val isSelected = uiState.selectedColor == palette
        val label = if (uiState.language == AppLanguage.ARABIC) palette.nameAr else palette.nameEn

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
              if (isSelected) DarkSurfaceElevated else Color.Transparent
            )
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) GoldPrimary else DarkBorder.copy(alpha = 0.5f),
              shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelectColor(palette) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("color_filter_${palette.id}"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(
                  if (palette == ColorPalette.ALL) Brush.sweepGradient(
                    listOf(Color(0xFFD4AF37), Color(0xFF10B981), Color(0xFF2563EB), Color(0xFFE11D48), Color(0xFFD4AF37))
                  ) else Brush.radialGradient(listOf(Color(android.graphics.Color.parseColor(palette.hex)), Color.Black))
                )
            )
            Text(
              text = label,
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = if (isSelected) GoldLight else Color.LightGray
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    // 4. Two-Column Wallpaper Grid with Infinite Scroll
    if (filteredWallpapers.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "۞",
            fontSize = 44.sp,
            color = GoldPrimary
          )
          Text(
            text = Strings.get("no_wallpapers_found"),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
          )
          Text(
            text = Strings.get("reset_filters"),
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = GoldLight,
            modifier = Modifier
              .clickable {
                onSelectCategory(WallpaperCategory.ALL)
                onSelectColor(ColorPalette.ALL)
                onSearchChange("")
              }
              .padding(8.dp)
          )
        }
      }
    } else {
      val displayedList = filteredWallpapers.take(uiState.displayedWallpaperCount)

      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
          .fillMaxSize()
          .testTag("wallpapers_grid")
      ) {
        items(displayedList, key = { it.id }) { wallpaper ->
          WallpaperGridCard(
            wallpaper = wallpaper,
            language = uiState.language,
            isFavorite = uiState.favorites.contains(wallpaper.id),
            isUserPremium = uiState.isPremiumUser,
            onToggleFavorite = { onToggleFavorite(wallpaper.id) },
            onClick = { onOpenPreview(wallpaper) }
          )
        }

        // Infinite scroll loading indicator item at bottom
        if (uiState.displayedWallpaperCount < filteredWallpapers.size) {
          item(span = { GridItemSpan(2) }) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                CircularProgressIndicator(
                  color = GoldPrimary,
                  strokeWidth = 2.dp,
                  modifier = Modifier.size(20.dp)
                )
                Text(
                  text = Strings.get("infinite_loading"),
                  fontFamily = CairoFontFamily,
                  fontSize = 12.sp,
                  color = GoldMetallic
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun WallpaperGridCard(
  wallpaper: Wallpaper,
  language: AppLanguage,
  isFavorite: Boolean,
  isUserPremium: Boolean,
  onToggleFavorite: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = if (language == AppLanguage.ARABIC) wallpaper.titleAr else wallpaper.title

  Box(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(0.68f)
      .clip(RoundedCornerShape(20.dp))
      .background(DarkSurfaceElevated)
      .border(
        width = if (wallpaper.isPremium) 1.5.dp else 1.dp,
        color = if (wallpaper.isPremium) DarkGoldBorder else DarkBorder,
        shape = RoundedCornerShape(20.dp)
      )
      .shadow(6.dp, RoundedCornerShape(20.dp))
      .clickable(onClick = onClick)
      .testTag("wallpaper_card_${wallpaper.id}")
  ) {
    // 1. Wallpaper Canvas Art Visual
    WallpaperCanvasVisual(
      wallpaper = wallpaper,
      modifier = Modifier.fillMaxSize()
    )

    // 2. Soft bottom vignette gradient for text readability
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xDD090D14), Color(0xFF090D14)),
            startY = 0f,
            endY = 220f
          )
        )
        .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
      Column {
        Text(
          text = title,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = Color.White,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = if (language == AppLanguage.ARABIC) wallpaper.category.titleAr else wallpaper.category.titleEn,
          fontFamily = CairoFontFamily,
          fontSize = 10.sp,
          color = GoldMetallic
        )
      }
    }

    // 3. Top Badges: Premium Lock Badge & Favorite Heart
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      // Premium Lock Badge
      if (wallpaper.isPremium) {
        val badgeModifier = if (isUserPremium) {
          Modifier.background(Color(0xCC10B981))
        } else {
          Modifier.background(Brush.horizontalGradient(listOf(GoldPrimary, GoldLight)))
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .then(badgeModifier)
            .padding(horizontal = 6.dp, vertical = 3.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Icon(
              imageVector = if (isUserPremium) Icons.Default.Bookmark else Icons.Default.Lock,
              contentDescription = "Lock",
              tint = if (isUserPremium) Color.White else PremiumBadgeText,
              modifier = Modifier.size(11.dp)
            )
            Text(
              text = if (isUserPremium) {
                if (language == AppLanguage.ARABIC) "مفتوح" else "UNLOCKED"
              } else {
                if (language == AppLanguage.ARABIC) "مميز" else "PRO"
              },
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              color = if (isUserPremium) Color.White else PremiumBadgeText
            )
          }
        }
      } else {
        Spacer(modifier = Modifier.width(1.dp))
      }

      // Favorite Bookmark Button
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Color(0x88000000))
          .clickable(onClick = onToggleFavorite),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
          contentDescription = "Favorite",
          tint = if (isFavorite) GoldPrimary else Color.White,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

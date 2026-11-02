package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.WallpaperData
import com.example.localization.LocalAppLanguage
import com.example.ui.components.ArabicWallsBottomNav
import com.example.ui.components.ArabicWallsTopBar
import com.example.ui.screens.AdminBulkGeneratorScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.QuoteEditorScreen
import com.example.ui.screens.WallpaperPreviewScreen
import com.example.ui.theme.ArabicWallsTheme
import com.example.ui.theme.DarkBackground
import com.example.util.configureAppCheck
import com.example.viewmodel.Screen
import com.example.viewmodel.WallpaperViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

  private val viewModel: WallpaperViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // STEP 1: Configure App Check with launch intent before any UI or Firebase operations
    configureAppCheck(this, intent)

    enableEdgeToEdge()
    setContent {
      ArabicWallsTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        // Collect message events (toasts/notifications)
        LaunchedEffect(Unit) {
          viewModel.messageEvents.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
          }
        }

        // Full RTL Support: Provide both LocalAppLanguage and LocalLayoutDirection
        CompositionLocalProvider(
          LocalAppLanguage provides uiState.language,
          LocalLayoutDirection provides uiState.language.layoutDirection
        ) {
          // Hardware / System Back Button handling
          BackHandler(enabled = uiState.currentScreen != Screen.HOME) {
            viewModel.navigateTo(Screen.HOME)
          }

          Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
              // Only show top bar when not in immersive full-screen preview
              if (uiState.currentScreen != Screen.PREVIEW) {
                ArabicWallsTopBar(
                  language = uiState.language,
                  isPremium = uiState.isPremiumUser,
                  onToggleLanguage = { viewModel.toggleLanguage() },
                  onOpenPremium = { viewModel.navigateTo(Screen.PREMIUM) }
                )
              }
            },
            bottomBar = {
              // Hide bottom bar in preview mode for full immersive view
              if (uiState.currentScreen != Screen.PREVIEW) {
                ArabicWallsBottomNav(
                  currentScreen = uiState.currentScreen,
                  onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
              }
            },
            containerColor = DarkBackground,
            modifier = Modifier.fillMaxSize()
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
            ) {
              AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
              ) { screen ->
                when (screen) {
                  Screen.HOME -> {
                    HomeScreen(
                      uiState = uiState,
                      onSelectCategory = { cat -> viewModel.selectCategory(cat) },
                      onSelectColor = { col -> viewModel.selectColor(col) },
                      onSearchChange = { q -> viewModel.updateSearchQuery(q) },
                      onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                      onOpenPreview = { wall -> viewModel.openWallpaperPreview(wall) },
                      onLoadMore = { viewModel.loadMoreWallpapers() }
                    )
                  }
                  Screen.PREVIEW -> {
                    val selected = uiState.selectedWallpaper ?: WallpaperData.sampleWallpapers.first()
                    WallpaperPreviewScreen(
                      wallpaper = selected,
                      language = uiState.language,
                      isFavorite = uiState.favorites.contains(selected.id),
                      isUserPremium = uiState.isPremiumUser,
                      isApplying = uiState.isApplyingWallpaper,
                      showSetDialog = uiState.showSetWallpaperDialog,
                      onToggleFavorite = { viewModel.toggleFavorite(selected.id) },
                      onShowSetDialog = { show -> viewModel.showSetWallpaperDialog(show) },
                      onApplyWallpaper = { target -> viewModel.applyWallpaper(target) },
                      onDownload = { viewModel.downloadCurrentWallpaper(isQuote = false) },
                      onOpenQuoteEditor = { wall -> viewModel.openQuoteEditorWithWallpaper(wall) },
                      onOpenPremium = { viewModel.navigateTo(Screen.PREMIUM) },
                      onBack = { viewModel.navigateTo(Screen.HOME) }
                    )
                  }
                  Screen.QUOTE_EDITOR -> {
                    val quoteState = uiState.quoteEditorState
                    if (quoteState != null) {
                      QuoteEditorScreen(
                        state = quoteState,
                        language = uiState.language,
                        onUpdateText = { text -> viewModel.updateQuoteText(text) },
                        onUpdateSource = { src -> viewModel.updateQuoteSource(src) },
                        onSelectPreset = { preset -> viewModel.applyQuotePreset(preset) },
                        onToggleFont = { isCairo -> viewModel.setQuoteFont(isCairo) },
                        onUpdateFontSize = { sz -> viewModel.setQuoteFontSize(sz) },
                        onSelectTextColor = { col -> viewModel.setQuoteTextColor(col) },
                        onSelectPosition = { pos -> viewModel.setQuotePosition(pos) },
                        onUpdateDimOverlay = { dim -> viewModel.setQuoteDimOverlay(dim) },
                        onApplyAsWallpaper = { viewModel.applyEditedQuoteWallpaper() },
                        onDownloadArtwork = { viewModel.downloadCurrentWallpaper(isQuote = true) }
                      )
                    }
                  }
                  Screen.FAVORITES -> {
                    val favList = uiState.allWallpapers.filter { uiState.favorites.contains(it.id) }
                    FavoritesScreen(
                      favoriteWallpapers = favList,
                      language = uiState.language,
                      isUserPremium = uiState.isPremiumUser,
                      onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                      onOpenPreview = { wall -> viewModel.openWallpaperPreview(wall) },
                      onExploreWallpapers = { viewModel.navigateTo(Screen.HOME) }
                    )
                  }
                  Screen.PREMIUM -> {
                    PremiumScreen(
                      isPremiumUser = uiState.isPremiumUser,
                      language = uiState.language,
                      onPurchasePlan = { plan -> viewModel.purchasePlan(plan) },
                      onToggleDemoPremium = { viewModel.toggleDemoPremium() }
                    )
                  }
                  Screen.ADMIN_BULK_GENERATOR -> {
                    AdminBulkGeneratorScreen(
                      state = uiState.bulkGeneratorState,
                      language = uiState.language,
                      onSelectCategory = { cat -> viewModel.selectBulkCategory(cat) },
                      onSelectQuantity = { qty -> viewModel.selectBulkQuantity(qty) },
                      onUpdateDailyLimit = { lim -> viewModel.updateDailyLimit(lim) },
                      onResetDailyCounter = { viewModel.resetDailyUsedCount() },
                      onStartGeneration = { viewModel.startBulkGeneration() },
                      onStopGeneration = { viewModel.stopBulkGeneration() },
                      onApproveCandidate = { id -> viewModel.approveCandidate(id) },
                      onRejectCandidate = { id -> viewModel.rejectCandidate(id) },
                      onRegenerateCandidate = { id -> viewModel.regenerateCandidate(id) },
                      onApproveAllGenerated = { viewModel.approveAllGeneratedCandidates() },
                      onRemoveDuplicates = { viewModel.removeDuplicatesAndNearDuplicates() },
                      onExportZip = { viewModel.exportApprovedWallpapersAsZip() }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

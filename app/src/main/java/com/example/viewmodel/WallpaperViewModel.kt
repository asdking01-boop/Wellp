package com.example.viewmodel

import android.app.Application
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PromptTemplateEngine
import com.example.data.WallpaperData
import com.example.localization.AppLanguage
import com.example.model.BulkGenCategory
import com.example.model.CandidateStatus
import com.example.model.ColorPalette
import com.example.model.GeneratedWallpaperCandidate
import com.example.model.PremiumPlan
import com.example.model.QuotePreset
import com.example.model.Wallpaper
import com.example.model.WallpaperCategory
import com.example.ui.components.createWallpaperBitmap
import com.example.util.ImageAnalysisAndExportUtil
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "WallpaperViewModel"
private const val PREFS_ADMIN = "arabicwalls_admin_prefs"
private const val KEY_DAILY_LIMIT = "daily_limit"
private const val KEY_DAILY_COUNT = "daily_count"
private const val KEY_DAILY_DATE = "daily_date"

enum class Screen {
  HOME,
  PREVIEW,
  QUOTE_EDITOR,
  FAVORITES,
  PREMIUM,
  ADMIN_BULK_GENERATOR
}

data class QuoteEditorState(
  val wallpaper: Wallpaper,
  val text: String = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
  val source: String = "سورة الرعد: 28",
  val isCairoFont: Boolean = false,
  val fontSizeSp: Float = 26f,
  val textColorHex: Int = 0xFFF6E27A.toInt(),
  val positionIndex: Int = 1,
  val dimBlurOverlay: Float = 0.45f
)

data class BulkGeneratorState(
  val selectedCategory: BulkGenCategory = BulkGenCategory.ARABIC_LETTERS,
  val selectedQuantity: Int = 10, // 10, 25, 50, 100
  val dailyLimit: Int = 50,
  val dailyUsedCount: Int = 0,
  val candidates: List<GeneratedWallpaperCandidate> = emptyList(),
  val isGenerating: Boolean = false,
  val completedInBatch: Int = 0,
  val totalInBatch: Int = 0,
  val duplicatesRemovedCount: Int = 0,
  val isExportingZip: Boolean = false,
  val lastExportedZipPath: String? = null
)

data class WallpaperUiState(
  val currentScreen: Screen = Screen.HOME,
  val language: AppLanguage = AppLanguage.ARABIC,
  val allWallpapers: List<Wallpaper> = WallpaperData.sampleWallpapers,
  val selectedCategory: WallpaperCategory = WallpaperCategory.ALL,
  val selectedColor: ColorPalette = ColorPalette.ALL,
  val searchQuery: String = "",
  val favorites: Set<String> = setOf("wall_letter_waw", "wall_geom_8star", "wall_occ_ramadan_lantern"),
  val isPremiumUser: Boolean = false,
  val selectedWallpaper: Wallpaper? = null,
  val quoteEditorState: QuoteEditorState? = null,
  val displayedWallpaperCount: Int = 8,
  val isLoadingMore: Boolean = false,
  val showSetWallpaperDialog: Boolean = false,
  val isApplyingWallpaper: Boolean = false,
  val bulkGeneratorState: BulkGeneratorState = BulkGeneratorState()
)

class WallpaperViewModel(application: Application) : AndroidViewModel(application) {

  private val _uiState = MutableStateFlow(WallpaperUiState())
  val uiState: StateFlow<WallpaperUiState> = _uiState.asStateFlow()

  private val _messageEvents = MutableSharedFlow<String>()
  val messageEvents: SharedFlow<String> = _messageEvents.asSharedFlow()

  private var bulkGenJob: Job? = null

  // Lazy initialization of Gemini Nano Banana 2.1 model via Firebase AI Logic Backend Proxy
  private val imageGenerationModel by lazy {
    val config = generationConfig {
      responseModalities = listOf(ResponseModality.IMAGE)
    }
    Firebase.ai.generativeModel(
      modelName = "gemini-nano-banana-2.1",
      generationConfig = config
    )
  }

  init {
    val initialWallpaper = WallpaperData.sampleWallpapers.first()
    val savedWallpapers = loadPersistedWallpapersFromStorage()
    val mergedWallpapers = if (savedWallpapers.isNotEmpty()) {
      val existingIds = savedWallpapers.map { it.id }.toSet()
      savedWallpapers + WallpaperData.sampleWallpapers.filterNot { existingIds.contains(it.id) }
    } else {
      WallpaperData.sampleWallpapers
    }

    val (limit, usedToday) = loadDailyQuotaState()

    _uiState.update {
      it.copy(
        allWallpapers = mergedWallpapers,
        selectedWallpaper = initialWallpaper,
        quoteEditorState = QuoteEditorState(wallpaper = initialWallpaper),
        bulkGeneratorState = it.bulkGeneratorState.copy(
          dailyLimit = limit,
          dailyUsedCount = usedToday
        )
      )
    }
  }

  private fun getTodayString(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
  }

  private fun loadDailyQuotaState(): Pair<Int, Int> {
    val prefs = getApplication<Application>().getSharedPreferences(PREFS_ADMIN, Context.MODE_PRIVATE)
    val limit = prefs.getInt(KEY_DAILY_LIMIT, 50)
    val savedDate = prefs.getString(KEY_DAILY_DATE, "")
    val today = getTodayString()
    return if (savedDate == today) {
      limit to prefs.getInt(KEY_DAILY_COUNT, 0)
    } else {
      prefs.edit().putString(KEY_DAILY_DATE, today).putInt(KEY_DAILY_COUNT, 0).apply()
      limit to 0
    }
  }

  private fun incrementDailyUsage(): Boolean {
    val prefs = getApplication<Application>().getSharedPreferences(PREFS_ADMIN, Context.MODE_PRIVATE)
    val today = getTodayString()
    val savedDate = prefs.getString(KEY_DAILY_DATE, "")
    var used = if (savedDate == today) prefs.getInt(KEY_DAILY_COUNT, 0) else 0
    val limit = _uiState.value.bulkGeneratorState.dailyLimit
    if (used >= limit) {
      return false
    }
    used += 1
    prefs.edit().putString(KEY_DAILY_DATE, today).putInt(KEY_DAILY_COUNT, used).apply()
    _uiState.update {
      it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(dailyUsedCount = used))
    }
    return true
  }

  fun updateDailyLimit(newLimit: Int) {
    val coerced = newLimit.coerceIn(4, 500)
    val prefs = getApplication<Application>().getSharedPreferences(PREFS_ADMIN, Context.MODE_PRIVATE)
    prefs.edit().putInt(KEY_DAILY_LIMIT, coerced).apply()
    _uiState.update {
      it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(dailyLimit = coerced))
    }
  }

  fun resetDailyUsedCount() {
    val prefs = getApplication<Application>().getSharedPreferences(PREFS_ADMIN, Context.MODE_PRIVATE)
    prefs.edit().putString(KEY_DAILY_DATE, getTodayString()).putInt(KEY_DAILY_COUNT, 0).apply()
    _uiState.update {
      it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(dailyUsedCount = 0))
    }
  }

  // Navigation
  fun navigateTo(screen: Screen) {
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun openWallpaperPreview(wallpaper: Wallpaper) {
    _uiState.update {
      it.copy(
        selectedWallpaper = wallpaper,
        currentScreen = Screen.PREVIEW
      )
    }
  }

  fun openQuoteEditorWithWallpaper(wallpaper: Wallpaper) {
    _uiState.update {
      it.copy(
        selectedWallpaper = wallpaper,
        quoteEditorState = QuoteEditorState(
          wallpaper = wallpaper,
          text = if (it.language == AppLanguage.ARABIC) "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ" else "Unquestionably, by the remembrance of Allah hearts are assured.",
          source = if (it.language == AppLanguage.ARABIC) "سورة الرعد: 28" else "Surah Ar-Ra'd: 28"
        ),
        currentScreen = Screen.QUOTE_EDITOR
      )
    }
  }

  // Language RTL toggle
  fun toggleLanguage() {
    _uiState.update {
      val next = if (it.language == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
      it.copy(language = next)
    }
  }

  // Category and Filter
  fun selectCategory(category: WallpaperCategory) {
    _uiState.update { it.copy(selectedCategory = category, displayedWallpaperCount = 8) }
  }

  fun selectColor(color: ColorPalette) {
    _uiState.update { it.copy(selectedColor = color, displayedWallpaperCount = 8) }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun resetFilters() {
    _uiState.update {
      it.copy(
        selectedCategory = WallpaperCategory.ALL,
        selectedColor = ColorPalette.ALL,
        searchQuery = ""
      )
    }
  }

  // Infinite Scroll Trigger
  fun loadMoreWallpapers() {
    if (_uiState.value.isLoadingMore) return
    viewModelScope.launch {
      _uiState.update { it.copy(isLoadingMore = true) }
      delay(400)
      _uiState.update {
        it.copy(
          displayedWallpaperCount = it.displayedWallpaperCount + 6,
          isLoadingMore = false
        )
      }
    }
  }

  // Favorites
  fun toggleFavorite(wallpaperId: String) {
    _uiState.update { state ->
      val newFavs = if (state.favorites.contains(wallpaperId)) {
        state.favorites - wallpaperId
      } else {
        state.favorites + wallpaperId
      }
      state.copy(favorites = newFavs)
    }
  }

  // Premium Management
  fun toggleDemoPremium() {
    _uiState.update { it.copy(isPremiumUser = !it.isPremiumUser) }
  }

  fun purchasePlan(plan: PremiumPlan) {
    viewModelScope.launch {
      _uiState.update { it.copy(isPremiumUser = true) }
      val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
        "تهانينا! تم تفعيل الباقة المميزة (${plan.titleAr}) بنجاح!"
      } else {
        "Congratulations! Premium Membership (${plan.titleEn}) activated!"
      }
      _messageEvents.emit(msg)
      navigateTo(Screen.HOME)
    }
  }

  // Quote Editor Updates
  fun updateQuoteText(newText: String) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(text = newText))
    }
  }

  fun updateQuoteSource(newSource: String) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(source = newSource))
    }
  }

  fun applyQuotePreset(preset: QuotePreset) {
    _uiState.update { state ->
      val isAr = state.language == AppLanguage.ARABIC
      state.copy(
        quoteEditorState = state.quoteEditorState?.copy(
          text = if (isAr) preset.arabicText else preset.englishText,
          source = if (isAr) preset.sourceAr else preset.sourceEn
        )
      )
    }
  }

  fun setQuoteFont(isCairo: Boolean) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(isCairoFont = isCairo))
    }
  }

  fun setQuoteFontSize(size: Float) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(fontSizeSp = size))
    }
  }

  fun setQuoteTextColor(colorHex: Int) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(textColorHex = colorHex))
    }
  }

  fun setQuotePosition(pos: Int) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(positionIndex = pos))
    }
  }

  fun setQuoteDimOverlay(dim: Float) {
    _uiState.update { state ->
      state.copy(quoteEditorState = state.quoteEditorState?.copy(dimBlurOverlay = dim))
    }
  }

  // =========================================================================
  // ADMIN BULK WALLPAPER GENERATOR LOGIC
  // =========================================================================

  fun selectBulkCategory(category: BulkGenCategory) {
    _uiState.update {
      it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(selectedCategory = category))
    }
  }

  fun selectBulkQuantity(quantity: Int) {
    _uiState.update {
      it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(selectedQuantity = quantity))
    }
  }

  /**
   * Starts bulk wallpaper generation:
   * - Builds `quantity` unique prompt combinations with mandatory suffix
   * - Runs in batches of 4 in parallel using Semaphore(4)
   * - Automatically retries up to 2 times on failure
   * - Respects daily limit cost control
   */
  fun startBulkGeneration() {
    val bulkState = _uiState.value.bulkGeneratorState
    if (bulkState.isGenerating) return

    val remainingQuota = bulkState.dailyLimit - bulkState.dailyUsedCount
    if (remainingQuota <= 0) {
      viewModelScope.launch {
        val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
          "تم الوصول إلى الحد اليومي (${bulkState.dailyLimit}). يرجى زيادة الحد اليومي أو تصفيره."
        } else {
          "Daily generation limit (${bulkState.dailyLimit}) reached. Increase or reset daily limit."
        }
        _messageEvents.emit(msg)
      }
      return
    }

    val category = bulkState.selectedCategory
    val quantity = bulkState.selectedQuantity
    val promptVariations = PromptTemplateEngine.generateUniquePrompts(category, quantity)

    val newCandidates = promptVariations.mapIndexed { idx, variation ->
      GeneratedWallpaperCandidate(
        id = "ai_${category.id}_${System.currentTimeMillis()}_$idx",
        bulkCategory = category,
        variation = variation,
        status = CandidateStatus.PENDING
      )
    }

    _uiState.update { state ->
      state.copy(
        bulkGeneratorState = state.bulkGeneratorState.copy(
          candidates = newCandidates + state.bulkGeneratorState.candidates,
          isGenerating = true,
          completedInBatch = 0,
          totalInBatch = newCandidates.size
        )
      )
    }

    bulkGenJob = viewModelScope.launch {
      // Semaphore of 4 ensures batches of 4 run in parallel
      val parallelBatchSemaphore = Semaphore(4)

      try {
        val deferredTasks = newCandidates.map { candidate ->
          async(Dispatchers.IO) {
            parallelBatchSemaphore.withPermit {
              generateSingleCandidateWithRetry(candidate.id, maxRetries = 2)
              _uiState.update { st ->
                st.copy(
                  bulkGeneratorState = st.bulkGeneratorState.copy(
                    completedInBatch = (st.bulkGeneratorState.completedInBatch + 1)
                      .coerceAtMost(st.bulkGeneratorState.totalInBatch)
                  )
                )
              }
            }
          }
        }
        deferredTasks.awaitAll()
      } catch (e: CancellationException) {
        throw e
      } finally {
        _uiState.update { st ->
          st.copy(bulkGeneratorState = st.bulkGeneratorState.copy(isGenerating = false))
        }
      }
    }
  }

  fun stopBulkGeneration() {
    bulkGenJob?.cancel()
    _uiState.update { state ->
      val updated = state.bulkGeneratorState.candidates.map { c ->
        if (c.status == CandidateStatus.PENDING || c.status == CandidateStatus.GENERATING) {
          c.copy(status = CandidateStatus.ERROR, errorMessage = "Cancelled by user")
        } else {
          c
        }
      }
      state.copy(
        bulkGeneratorState = state.bulkGeneratorState.copy(
          isGenerating = false,
          candidates = updated
        )
      )
    }
  }

  /**
   * Generates a single candidate image via Firebase AI Logic (`gemini-nano-banana-2.1`)
   * with automatic retry on failure (up to `maxRetries` attempts) and 60s timeout per attempt.
   */
  private suspend fun generateSingleCandidateWithRetry(candidateId: String, maxRetries: Int = 2) {
    val currentCandidate = _uiState.value.bulkGeneratorState.candidates.find { it.id == candidateId } ?: return

    // Check daily cost limit before calling Gemini API
    if (!incrementDailyUsage()) {
      updateCandidate(candidateId) {
        it.copy(
          status = CandidateStatus.ERROR,
          errorMessage = "Daily cost limit reached (${_uiState.value.bulkGeneratorState.dailyLimit})"
        )
      }
      return
    }

    var attempt = 0
    var lastError: String? = null

    while (attempt <= maxRetries) {
      val currentAttempt = attempt
      updateCandidate(candidateId) {
        it.copy(
          status = CandidateStatus.GENERATING,
          retryCount = currentAttempt,
          errorMessage = null
        )
      }

      try {
        // 60-second realistic timeout for Gemini image generation
        val bitmap = withTimeout(60_000L) {
          val response = imageGenerationModel.generateContent(currentCandidate.variation.fullPrompt)
          response.candidates.firstOrNull()
            ?.content?.parts?.filterIsInstance<ImagePart>()
            ?.firstOrNull()
            ?.image
        }

        if (bitmap != null) {
          val savedFile = saveGeneratedBitmapToDisk(candidateId, bitmap)
          val pHash = ImageAnalysisAndExportUtil.computePerceptualHash(bitmap)
          val dominantColors = ImageAnalysisAndExportUtil.extractDominantColors(bitmap, 3)

          updateCandidate(candidateId) {
            it.copy(
              status = CandidateStatus.GENERATED,
              bitmapPath = savedFile.absolutePath,
              perceptualHash = pHash,
              extractedColors = dominantColors,
              errorMessage = null
            )
          }
          return
        } else {
          lastError = "Model returned an empty image response."
        }
      } catch (e: CancellationException) {
        if (e is TimeoutCancellationException) {
          Log.e(TAG, "Image generation timed out (attempt ${attempt + 1}) for $candidateId", e)
          lastError = "Timed out after 60s."
        } else {
          throw e
        }
      } catch (e: Exception) {
        Log.e(TAG, "Firebase AI image generation failed (attempt ${attempt + 1}) for $candidateId", e)
        lastError = e.localizedMessage ?: "Generation failed."
      }

      attempt++
      if (attempt <= maxRetries) {
        delay(1200L * attempt)
      }
    }

    // All retries exhausted — surface real error to UI
    updateCandidate(candidateId) {
      it.copy(
        status = CandidateStatus.ERROR,
        errorMessage = lastError ?: "Failed after ${maxRetries + 1} attempts."
      )
    }
  }

  private fun saveGeneratedBitmapToDisk(candidateId: String, bitmap: Bitmap): File {
    val dir = File(getApplication<Application>().filesDir, "generated_wallpapers")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "$candidateId.png")
    FileOutputStream(file).use { out ->
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return file
  }

  private fun updateCandidate(
    candidateId: String,
    transform: (GeneratedWallpaperCandidate) -> GeneratedWallpaperCandidate
  ) {
    _uiState.update { state ->
      val updatedList = state.bulkGeneratorState.candidates.map { c ->
        if (c.id == candidateId) transform(c) else c
      }
      state.copy(
        bulkGeneratorState = state.bulkGeneratorState.copy(candidates = updatedList)
      )
    }
  }

  /**
   * Regenerates a single item in the review grid with a fresh unique prompt variation.
   */
  fun regenerateCandidate(candidateId: String) {
    val existing = _uiState.value.bulkGeneratorState.candidates.find { it.id == candidateId } ?: return
    val allUsedPrompts = _uiState.value.bulkGeneratorState.candidates.map { it.variation.fullPrompt }.toSet()
    val newVariation = PromptTemplateEngine.generateSingleReplacementPrompt(existing.bulkCategory, allUsedPrompts)

    updateCandidate(candidateId) {
      it.copy(
        variation = newVariation,
        status = CandidateStatus.PENDING,
        errorMessage = null,
        retryCount = 0
      )
    }

    viewModelScope.launch(Dispatchers.IO) {
      generateSingleCandidateWithRetry(candidateId, maxRetries = 2)
    }
  }

  /**
   * Rejects a candidate in the review grid.
   */
  fun rejectCandidate(candidateId: String) {
    updateCandidate(candidateId) {
      it.copy(status = CandidateStatus.REJECTED)
    }
  }

  /**
   * Approves a generated candidate:
   * - Extracts dominant colors from the generated bitmap (if not already extracted)
   * - Creates Arabic & English titles and tags
   * - Randomly marks ~30% as isPremium
   * - Saves to `wallpapers.json` and adds immediately to the main app's wallpaper list
   */
  fun approveCandidate(candidateId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val candidate = _uiState.value.bulkGeneratorState.candidates.find { it.id == candidateId } ?: return@launch
      val path = candidate.bitmapPath ?: return@launch
      val file = File(path)
      if (!file.exists()) return@launch

      val colors = if (candidate.extractedColors.isNotEmpty()) {
        candidate.extractedColors
      } else {
        val bmp = BitmapFactory.decodeFile(path)
        if (bmp != null) ImageAnalysisAndExportUtil.extractDominantColors(bmp, 3) else listOf("#D4AF37", "#090D14")
      }

      val (titleAr, titleEn, tags) = ImageAnalysisAndExportUtil.buildApprovalMetadata(
        category = candidate.bulkCategory,
        variation = candidate.variation,
        indexSeed = candidate.id.hashCode()
      )
      val isPrem = ImageAnalysisAndExportUtil.shouldMarkPremium()

      val approvedCandidate = candidate.copy(
        status = CandidateStatus.APPROVED,
        extractedColors = colors,
        generatedTags = tags,
        titleAr = titleAr,
        titleEn = titleEn,
        isPremium = isPrem
      )

      val newWallpaper = Wallpaper(
        id = approvedCandidate.id,
        title = titleEn,
        titleAr = titleAr,
        category = approvedCandidate.bulkCategory.mappedWallpaperCategory,
        colors = colors,
        tags = tags,
        isPremium = isPrem,
        artist = "ArabicWalls AI Studio",
        downloads = 1,
        likes = 1,
        subtextArabic = candidate.variation.subjectAr,
        subtextEnglish = candidate.variation.subjectVar,
        imageUrl = file.absolutePath
      )

      _uiState.update { state ->
        val updatedCandidates = state.bulkGeneratorState.candidates.map {
          if (it.id == candidateId) approvedCandidate else it
        }
        val updatedWallpapers = listOf(newWallpaper) + state.allWallpapers.filterNot { it.id == newWallpaper.id }
        ImageAnalysisAndExportUtil.saveWallpapersJsonToInternalStorage(getApplication(), updatedWallpapers)

        state.copy(
          allWallpapers = updatedWallpapers,
          bulkGeneratorState = state.bulkGeneratorState.copy(candidates = updatedCandidates)
        )
      }

      val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
        "تم اعتماد الخلفية وإضافتها للتطبيق وملف JSON بنجاح!"
      } else {
        "Wallpaper approved, added to main app & saved to JSON!"
      }
      _messageEvents.emit(msg)
    }
  }

  /**
   * Approves all currently GENERATED candidates in one click.
   */
  fun approveAllGeneratedCandidates() {
    val generatedIds = _uiState.value.bulkGeneratorState.candidates
      .filter { it.status == CandidateStatus.GENERATED }
      .map { it.id }
    generatedIds.forEach { id -> approveCandidate(id) }
  }

  /**
   * Removes duplicate and near-duplicate candidates based on perceptual 64-bit dHash
   * (Hamming distance <= 8) or identical normalized prompts.
   */
  fun removeDuplicatesAndNearDuplicates() {
    viewModelScope.launch(Dispatchers.Default) {
      val currentList = _uiState.value.bulkGeneratorState.candidates
      val keptHashes = mutableListOf<Long>()
      val keptPrompts = mutableSetOf<String>()
      var removedCount = 0

      val deduplicated = currentList.map { candidate ->
        if (candidate.status == CandidateStatus.REJECTED || candidate.status == CandidateStatus.DUPLICATE_REMOVED) {
          candidate
        } else {
          val promptKey = candidate.variation.fullPrompt.trim().lowercase(Locale.ROOT)
          val isPromptDup = !keptPrompts.add(promptKey)

          val pHash = candidate.perceptualHash
          val isVisualNearDup = if (pHash != null) {
            keptHashes.any { existingHash ->
              ImageAnalysisAndExportUtil.hammingDistance(existingHash, pHash) <= 8
            }
          } else {
            false
          }

          if (isPromptDup || isVisualNearDup) {
            removedCount++
            candidate.copy(status = CandidateStatus.DUPLICATE_REMOVED)
          } else {
            if (pHash != null) keptHashes.add(pHash)
            candidate
          }
        }
      }

      _uiState.update { state ->
        state.copy(
          bulkGeneratorState = state.bulkGeneratorState.copy(
            candidates = deduplicated,
            duplicatesRemovedCount = state.bulkGeneratorState.duplicatesRemovedCount + removedCount
          )
        )
      }

      val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
        "تم فحص الصور واستبعاد $removedCount صورة مكررة أو متشابهة."
      } else {
        "Deduplication complete: $removedCount duplicate/near-duplicate item(s) removed."
      }
      _messageEvents.emit(msg)
    }
  }

  /**
   * Exports all approved images and `wallpapers.json` into a ZIP archive.
   */
  fun exportApprovedWallpapersAsZip() {
    val bulkState = _uiState.value.bulkGeneratorState
    if (bulkState.isExportingZip) return

    viewModelScope.launch(Dispatchers.IO) {
      _uiState.update {
        it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(isExportingZip = true))
      }
      try {
        val approved = _uiState.value.bulkGeneratorState.candidates.filter {
          it.status == CandidateStatus.APPROVED
        }
        val zipLocation = ImageAnalysisAndExportUtil.exportApprovedAsZip(
          context = getApplication(),
          approvedCandidates = approved,
          allWallpapers = _uiState.value.allWallpapers
        )
        _uiState.update {
          it.copy(
            bulkGeneratorState = it.bulkGeneratorState.copy(
              isExportingZip = false,
              lastExportedZipPath = zipLocation
            )
          )
        }
        val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
          "تم تصدير ملف ZIP بنجاح إلى: $zipLocation"
        } else {
          "Exported ZIP archive to: $zipLocation"
        }
        _messageEvents.emit(msg)
      } catch (e: Exception) {
        Log.e(TAG, "Failed to export ZIP", e)
        _uiState.update {
          it.copy(bulkGeneratorState = it.bulkGeneratorState.copy(isExportingZip = false))
        }
        _messageEvents.emit("ZIP Export Error: ${e.localizedMessage}")
      }
    }
  }

  private fun loadPersistedWallpapersFromStorage(): List<Wallpaper> {
    return try {
      val file = File(getApplication<Application>().filesDir, "wallpapers.json")
      if (!file.exists()) return emptyList()
      val jsonStr = file.readText()
      val arr = JSONArray(jsonStr)
      val list = mutableListOf<Wallpaper>()
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        val id = obj.optString("id")
        val titleEn = obj.optString("title_en", obj.optString("title", "Arabic Wallpaper"))
        val titleAr = obj.optString("title_ar", obj.optString("titleAr", "خلفية عربية"))
        val catName = obj.optString("category", "ARABIC_LETTERS")
        val category = runCatching { WallpaperCategory.valueOf(catName) }.getOrDefault(WallpaperCategory.ARABIC_LETTERS)

        val colorsArr = obj.optJSONArray("colors")
        val colors = mutableListOf<String>()
        if (colorsArr != null) {
          for (c in 0 until colorsArr.length()) colors.add(colorsArr.getString(c))
        }

        val tagsArr = obj.optJSONArray("tags")
        val tags = mutableListOf<String>()
        if (tagsArr != null) {
          for (t in 0 until tagsArr.length()) tags.add(tagsArr.getString(t))
        }

        val isPrem = obj.optBoolean("isPremium", false)
        val imageUrl = obj.optString("imageUrl", "")

        // Only restore if it's an AI-generated item with an existing local file
        if (id.startsWith("ai_") && imageUrl.isNotBlank() && File(imageUrl).exists()) {
          list.add(
            Wallpaper(
              id = id,
              title = titleEn,
              titleAr = titleAr,
              category = category,
              colors = colors.ifEmpty { listOf("#D4AF37", "#090D14") },
              tags = tags,
              isPremium = isPrem,
              imageUrl = imageUrl
            )
          )
        }
      }
      list
    } catch (e: Exception) {
      Log.e(TAG, "Error loading persisted wallpapers.json", e)
      emptyList()
    }
  }

  // Apply Wallpaper via Android WallpaperManager
  fun showSetWallpaperDialog(show: Boolean) {
    _uiState.update { it.copy(showSetWallpaperDialog = show) }
  }

  fun applyWallpaper(whichTarget: Int) {
    val wallpaper = _uiState.value.selectedWallpaper ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isApplyingWallpaper = true, showSetWallpaperDialog = false) }
      withContext(Dispatchers.IO) {
        try {
          val context = getApplication<Application>()
          val bitmap = createWallpaperBitmap(context, wallpaper)
          val wallpaperManager = WallpaperManager.getInstance(context)

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (whichTarget) {
              1 -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
              2 -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
              else -> wallpaperManager.setBitmap(bitmap)
            }
          } else {
            wallpaperManager.setBitmap(bitmap)
          }

          val successMsg = if (_uiState.value.language == AppLanguage.ARABIC) {
            "تم تعيين الخلفية على جهازك بنجاح!"
          } else {
            "Wallpaper successfully set on your device!"
          }
          _messageEvents.emit(successMsg)
        } catch (e: Exception) {
          val errorMsg = if (_uiState.value.language == AppLanguage.ARABIC) {
            "تعذر تعيين الخلفية: ${e.localizedMessage ?: "خطأ غير متوقع"}"
          } else {
            "Could not apply wallpaper: ${e.localizedMessage ?: "Unexpected error"}"
          }
          _messageEvents.emit(errorMsg)
        } finally {
          _uiState.update { it.copy(isApplyingWallpaper = false) }
        }
      }
    }
  }

  fun applyEditedQuoteWallpaper() {
    val quoteState = _uiState.value.quoteEditorState ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isApplyingWallpaper = true) }
      withContext(Dispatchers.IO) {
        try {
          val context = getApplication<Application>()
          val bitmap = createWallpaperBitmap(
            context = context,
            wallpaper = quoteState.wallpaper,
            quoteText = quoteState.text,
            quoteSource = quoteState.source,
            fontCairo = quoteState.isCairoFont,
            fontSizeSp = quoteState.fontSizeSp,
            fontColorHex = quoteState.textColorHex,
            dimAlpha = quoteState.dimBlurOverlay
          )
          val wallpaperManager = WallpaperManager.getInstance(context)
          wallpaperManager.setBitmap(bitmap)

          val successMsg = if (_uiState.value.language == AppLanguage.ARABIC) {
            "تم تطبيق تصميم الاقتباس كخلفية للجهاز بنجاح!"
          } else {
            "Quote artwork successfully applied as wallpaper!"
          }
          _messageEvents.emit(successMsg)
        } catch (e: Exception) {
          _messageEvents.emit("Error: ${e.localizedMessage}")
        } finally {
          _uiState.update { it.copy(isApplyingWallpaper = false) }
        }
      }
    }
  }

  // Download / Save to Gallery
  fun downloadCurrentWallpaper(isQuote: Boolean = false) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        try {
          val context = getApplication<Application>()
          val wallpaper = _uiState.value.selectedWallpaper ?: WallpaperData.sampleWallpapers.first()
          val quoteState = _uiState.value.quoteEditorState

          val bitmap = if (isQuote && quoteState != null) {
            createWallpaperBitmap(
              context = context,
              wallpaper = quoteState.wallpaper,
              quoteText = quoteState.text,
              quoteSource = quoteState.source,
              fontCairo = quoteState.isCairoFont,
              fontSizeSp = quoteState.fontSizeSp,
              fontColorHex = quoteState.textColorHex,
              dimAlpha = quoteState.dimBlurOverlay
            )
          } else {
            createWallpaperBitmap(context, wallpaper)
          }

          val filename = "ArabicWalls_${System.currentTimeMillis()}.png"
          val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/ArabicWalls")
            }
          }

          val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
          if (uri != null) {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
            outputStream?.use {
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
          }

          val msg = if (_uiState.value.language == AppLanguage.ARABIC) {
            "تم حفظ الخلفية بنجاح في ألبوم الصور!"
          } else {
            "Wallpaper saved to Pictures/ArabicWalls!"
          }
          _messageEvents.emit(msg)
        } catch (e: Exception) {
          val err = if (_uiState.value.language == AppLanguage.ARABIC) {
            "حدث خطأ أثناء حفظ الصورة"
          } else {
            "Error saving image: ${e.localizedMessage}"
          }
          _messageEvents.emit(err)
        }
      }
    }
  }
}

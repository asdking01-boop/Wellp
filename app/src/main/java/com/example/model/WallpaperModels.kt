package com.example.model

enum class WallpaperCategory(val id: String, val titleAr: String, val titleEn: String) {
  ALL("all", "الكل", "All"),
  ARABIC_LETTERS("arabic_letters", "حروف عربية", "Arabic Letters"),
  NATURE("nature", "طبيعة", "Nature"),
  OCCASIONS("occasions", "مناسبات", "Occasions"),
  GEOMETRIC("geometric", "زخارف هندسية", "Geometric"),
  DARK("dark", "داكنة", "Dark"),
  CALLIGRAPHY("calligraphy", "خط عربي", "Calligraphy"),
  RAMADAN("ramadan", "رمضان", "Ramadan"),
  EID("eid", "العيد", "Eid")
}

enum class ColorPalette(val id: String, val hex: String, val nameAr: String, val nameEn: String) {
  ALL("all", "#D4AF37", "الكل", "All"),
  GOLD("gold", "#D4AF37", "ذهبي", "Gold"),
  EMERALD("emerald", "#10B981", "زمردي", "Emerald"),
  SAPPHIRE("sapphire", "#2563EB", "أزرق ملكي", "Sapphire"),
  RUBY("ruby", "#E11D48", "ياقوتي", "Ruby"),
  CHARCOAL("charcoal", "#1E293B", "فحمي", "Charcoal"),
  AMETHYST("amethyst", "#8B5CF6", "بنفسجي", "Amethyst"),
  AMBER("amber", "#F59E0B", "عنبري", "Amber")
}

enum class WallpaperPatternType {
  CALLIGRAPHY_GLYPH,
  GEOMETRIC_STAR,
  ARABESQUE_ROSETTE,
  CRESCENT_DUNES,
  ISLAMIC_ARCH,
  GOLD_ARABIC_LETTER,
  MINIMAL_DARK_MOSAIC,
  RAMADAN_LANTERN,
  NATURE_OASIS
}

data class Wallpaper(
  val id: String,
  val title: String, // title_en
  val titleAr: String, // title_ar
  val category: WallpaperCategory,
  val colors: List<String>,
  val tags: List<String>,
  val isPremium: Boolean,
  val artist: String = "ArabicWalls Studio",
  val downloads: Int = 1200,
  val likes: Int = 450,
  val patternType: WallpaperPatternType = WallpaperPatternType.GEOMETRIC_STAR,
  val primaryGradientStart: Long = 0xFF0D1424,
  val primaryGradientEnd: Long = 0xFF06090F,
  val accentColor: Long = 0xFFD4AF37,
  val glyphOrLetter: String = "الله",
  val subtextArabic: String = "سبحان الله وبحمده",
  val subtextEnglish: String = "Glory be to Allah",
  val imageUrl: String = "" // Local file URI or remote URL
)

data class QuotePreset(
  val id: String,
  val arabicText: String,
  val englishText: String,
  val sourceAr: String,
  val sourceEn: String,
  val category: String
)

enum class PlanDuration {
  MONTHLY,
  YEARLY,
  LIFETIME
}

data class PremiumPlan(
  val duration: PlanDuration,
  val priceAr: String,
  val priceEn: String,
  val titleAr: String,
  val titleEn: String,
  val tagAr: String? = null,
  val tagEn: String? = null,
  val isPopular: Boolean = false
)

enum class BulkGenCategory(
  val id: String,
  val titleAr: String,
  val titleEn: String,
  val mappedWallpaperCategory: WallpaperCategory
) {
  ARABIC_LETTERS("arabic_letters", "حروف عربية", "Arabic Letters", WallpaperCategory.ARABIC_LETTERS),
  NATURE("nature", "طبيعة", "Nature", WallpaperCategory.NATURE),
  OCCASIONS("occasions", "مناسبات", "Occasions", WallpaperCategory.OCCASIONS),
  GEOMETRIC("geometric", "زخارف هندسية", "Geometric", WallpaperCategory.GEOMETRIC),
  DARK("dark", "داكنة", "Dark", WallpaperCategory.DARK),
  RAMADAN("ramadan", "رمضان", "Ramadan", WallpaperCategory.RAMADAN),
  EID("eid", "العيد", "Eid", WallpaperCategory.EID)
}

enum class CandidateStatus {
  PENDING,
  GENERATING,
  GENERATED,
  APPROVED,
  REJECTED,
  DUPLICATE_REMOVED,
  ERROR
}

data class PromptVariation(
  val fullPrompt: String,
  val colorVar: String,
  val styleVar: String,
  val subjectVar: String,
  val lightingVar: String,
  val patternVar: String,
  val subjectAr: String,
  val styleAr: String,
  val colorAr: String
)

data class GeneratedWallpaperCandidate(
  val id: String,
  val bulkCategory: BulkGenCategory,
  val variation: PromptVariation,
  val status: CandidateStatus = CandidateStatus.PENDING,
  val bitmapPath: String? = null,
  val perceptualHash: Long? = null,
  val retryCount: Int = 0,
  val errorMessage: String? = null,
  // Metadata filled upon approval
  val extractedColors: List<String> = emptyList(),
  val generatedTags: List<String> = emptyList(),
  val titleAr: String = "",
  val titleEn: String = "",
  val isPremium: Boolean = false
)

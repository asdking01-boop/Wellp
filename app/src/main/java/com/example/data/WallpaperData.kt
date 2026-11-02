package com.example.data

import com.example.model.ColorPalette
import com.example.model.PlanDuration
import com.example.model.PremiumPlan
import com.example.model.QuotePreset
import com.example.model.Wallpaper
import com.example.model.WallpaperCategory
import com.example.model.WallpaperPatternType

object WallpaperData {

  val sampleWallpapers: List<Wallpaper> = listOf(
    // Arabic Letters
    Wallpaper(
      id = "wall_letter_waw",
      title = "The Mystical Waw (و)",
      titleAr = "حرف الواو الروحاني",
      category = WallpaperCategory.ARABIC_LETTERS,
      colors = listOf("#D4AF37", "#090D14", "#1E293B"),
      tags = listOf("Letters", "Calligraphy", "Gold", "Minimalist"),
      isPremium = false,
      patternType = WallpaperPatternType.GOLD_ARABIC_LETTER,
      primaryGradientStart = 0xFF0D1424,
      primaryGradientEnd = 0xFF050810,
      accentColor = 0xFFD4AF37,
      glyphOrLetter = "و",
      subtextArabic = "حرف الواو - التناغم والوصل",
      subtextEnglish = "Letter Waw - Harmony & Divine Connection",
      downloads = 3420,
      likes = 1290
    ),
    Wallpaper(
      id = "wall_letter_noon",
      title = "The Celestial Noon (ن)",
      titleAr = "حرف النون وسحر القلم",
      category = WallpaperCategory.ARABIC_LETTERS,
      colors = listOf("#10B981", "#061A14", "#D4AF37"),
      tags = listOf("Letters", "Emerald", "Quranic", "Ink"),
      isPremium = true,
      patternType = WallpaperPatternType.GOLD_ARABIC_LETTER,
      primaryGradientStart = 0xFF062319,
      primaryGradientEnd = 0xFF020E0A,
      accentColor = 0xFF10B981,
      glyphOrLetter = "ن",
      subtextArabic = "ن وَالْقَلَمِ وَمَا يَسْطُرُونَ",
      subtextEnglish = "Noon, by the pen and what they inscribe",
      downloads = 4120,
      likes = 1840
    ),
    Wallpaper(
      id = "wall_letter_ha",
      title = "Royal Letter Ha (ح)",
      titleAr = "حرف الحاء الملكي",
      category = WallpaperCategory.ARABIC_LETTERS,
      colors = listOf("#2563EB", "#0B1528", "#E5C07B"),
      tags = listOf("Letters", "Sapphire", "Thuluth"),
      isPremium = false,
      patternType = WallpaperPatternType.GOLD_ARABIC_LETTER,
      primaryGradientStart = 0xFF0E1A33,
      primaryGradientEnd = 0xFF060D1A,
      accentColor = 0xFF60A5FA,
      glyphOrLetter = "ح",
      subtextArabic = "حرف الحاء - خط الثلث العربي",
      subtextEnglish = "Letter Ha - Thuluth Script",
      downloads = 1980,
      likes = 760
    ),
    Wallpaper(
      id = "wall_letter_alif",
      title = "The Majestic Alif (ا)",
      titleAr = "حرف الألف الصامد",
      category = WallpaperCategory.ARABIC_LETTERS,
      colors = listOf("#D4AF37", "#1E1A11", "#0D0C07"),
      tags = listOf("Letters", "Alif", "Gold", "Strength"),
      isPremium = true,
      patternType = WallpaperPatternType.GOLD_ARABIC_LETTER,
      primaryGradientStart = 0xFF1A140A,
      primaryGradientEnd = 0xFF0A0804,
      accentColor = 0xFFF6E27A,
      glyphOrLetter = "ا",
      subtextArabic = "ألف الاستقامة والجمال",
      subtextEnglish = "Alif of Rectitude & Grace",
      downloads = 2850,
      likes = 940
    ),

    // Nature
    Wallpaper(
      id = "wall_nature_dunes",
      title = "Moonlit Golden Dunes",
      titleAr = "رمال الصحراء الذهبية تحت ضوء القمر",
      category = WallpaperCategory.NATURE,
      colors = listOf("#F59E0B", "#1C140A", "#0C0A05"),
      tags = listOf("Desert", "Dunes", "Moonlight", "Serenity"),
      isPremium = false,
      patternType = WallpaperPatternType.CRESCENT_DUNES,
      primaryGradientStart = 0xFF241608,
      primaryGradientEnd = 0xFF0D0803,
      accentColor = 0xFFFBBF24,
      glyphOrLetter = "صحراء",
      subtextArabic = "سكينة الصحراء تحت ضوء الهلال",
      subtextEnglish = "Desert Serenity Beneath the Crescent",
      downloads = 5120,
      likes = 2390
    ),
    Wallpaper(
      id = "wall_nature_emerald_oasis",
      title = "Emerald Palm Oasis",
      titleAr = "واحة النخيل الزمردية",
      category = WallpaperCategory.NATURE,
      colors = listOf("#10B981", "#064E3B", "#021A13"),
      tags = listOf("Nature", "Palms", "Emerald", "Oasis"),
      isPremium = true,
      patternType = WallpaperPatternType.NATURE_OASIS,
      primaryGradientStart = 0xFF063327,
      primaryGradientEnd = 0xFF021610,
      accentColor = 0xFF34D399,
      glyphOrLetter = "واحة",
      subtextArabic = "ظلال النخيل والهدوء الفطري",
      subtextEnglish = "Palm Shadows & Tranquility",
      downloads = 3890,
      likes = 1450
    ),
    Wallpaper(
      id = "wall_nature_starlit_night",
      title = "Starlit Midnight Desert",
      titleAr = "ليلة الصحراء المرصعة بالنجوم",
      category = WallpaperCategory.NATURE,
      colors = listOf("#2563EB", "#0F172A", "#020617"),
      tags = listOf("Night", "Stars", "Cosmos", "Nature"),
      isPremium = false,
      patternType = WallpaperPatternType.CRESCENT_DUNES,
      primaryGradientStart = 0xFF0B1938,
      primaryGradientEnd = 0xFF030712,
      accentColor = 0xFF93C5FD,
      glyphOrLetter = "نجوم",
      subtextArabic = "وَهُوَ الَّذِي جَعَلَ لَكُمُ النُّجُومَ لِتَهْتَدُوا بِهَا",
      subtextEnglish = "And He who made the stars for your guidance",
      downloads = 6320,
      likes = 3100
    ),

    // Occasions (Ramadan, Eid)
    Wallpaper(
      id = "wall_occ_ramadan_lantern",
      title = "Ramadan Kareem Lanterns",
      titleAr = "فوانيس رمضان المبارك",
      category = WallpaperCategory.OCCASIONS,
      colors = listOf("#D4AF37", "#1E150A", "#0F0B05"),
      tags = listOf("Ramadan", "Fanous", "Crescent", "Blessings"),
      isPremium = false,
      patternType = WallpaperPatternType.RAMADAN_LANTERN,
      primaryGradientStart = 0xFF2A1C0C,
      primaryGradientEnd = 0xFF0D0803,
      accentColor = 0xFFFCD34D,
      glyphOrLetter = "رمضان كريم",
      subtextArabic = "شهر الخير والبركات والقرآن",
      subtextEnglish = "Month of Blessings and Revelation",
      downloads = 9800,
      likes = 5400
    ),
    Wallpaper(
      id = "wall_occ_eid_mubarak",
      title = "Eid Mubarak Golden Crest",
      titleAr = "عيد مبارك بالذهب الخالص",
      category = WallpaperCategory.OCCASIONS,
      colors = listOf("#D4AF37", "#E11D48", "#1A0B10"),
      tags = listOf("Eid", "Celebration", "Gold", "Joy"),
      isPremium = true,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF2D0F18,
      primaryGradientEnd = 0xFF0F0408,
      accentColor = 0xFFFBBF24,
      glyphOrLetter = "عيد مبارك",
      subtextArabic = "تقبل الله طاعاتكم وكل عام وأنتم بخير",
      subtextEnglish = "May Allah accept your good deeds",
      downloads = 7650,
      likes = 3980
    ),
    Wallpaper(
      id = "wall_occ_laylatul_qadr",
      title = "Night of Decree Radiance",
      titleAr = "إشراق ليلة القدر المباركة",
      category = WallpaperCategory.OCCASIONS,
      colors = listOf("#8B5CF6", "#170B28", "#080312"),
      tags = listOf("LailatAlQadr", "Ramadan", "Peace", "Angels"),
      isPremium = true,
      patternType = WallpaperPatternType.ARABESQUE_ROSETTE,
      primaryGradientStart = 0xFF22113D,
      primaryGradientEnd = 0xFF0B0416,
      accentColor = 0xFFC084FC,
      glyphOrLetter = "سَلَامٌ هِيَ",
      subtextArabic = "سَلَامٌ هِيَ حَتَّى مَطْلَعِ الْفَجْرِ",
      subtextEnglish = "Peace it is until the emergence of dawn",
      downloads = 4520,
      likes = 2100
    ),

    // Geometric
    Wallpaper(
      id = "wall_geom_8star",
      title = "Andalusian 8-Pointed Star",
      titleAr = "نجمة أندلسية مثمنة ذهبية",
      category = WallpaperCategory.GEOMETRIC,
      colors = listOf("#D4AF37", "#0F1A2A", "#050912"),
      tags = listOf("Geometric", "Star", "Andalusia", "Pattern"),
      isPremium = false,
      patternType = WallpaperPatternType.GEOMETRIC_STAR,
      primaryGradientStart = 0xFF101B2E,
      primaryGradientEnd = 0xFF050910,
      accentColor = 0xFFD4AF37,
      glyphOrLetter = "زخرفة",
      subtextArabic = "تطريز هندسي إسلامي أصيل",
      subtextEnglish = "Authentic Islamic Geometric Rosette",
      downloads = 5890,
      likes = 2450
    ),
    Wallpaper(
      id = "wall_geom_moroccan_rosette",
      title = "Moroccan Rosette Lattice",
      titleAr = "زليج مغربي فاخر متداخل",
      category = WallpaperCategory.GEOMETRIC,
      colors = listOf("#10B981", "#1E293B", "#0D1117"),
      tags = listOf("Zellij", "Morocco", "Emerald", "Architecture"),
      isPremium = true,
      patternType = WallpaperPatternType.ARABESQUE_ROSETTE,
      primaryGradientStart = 0xFF0E2820,
      primaryGradientEnd = 0xFF05120E,
      accentColor = 0xFF10B981,
      glyphOrLetter = "زليج",
      subtextArabic = "تناغم الأشكال الهندسية في العمارة الإسلامية",
      subtextEnglish = "Harmonious Geometry in Islamic Architecture",
      downloads = 3210,
      likes = 1600
    ),
    Wallpaper(
      id = "wall_geom_arch_corridor",
      title = "Sacred Islamic Arch Portal",
      titleAr = "قوس إسلامي مقدس",
      category = WallpaperCategory.GEOMETRIC,
      colors = listOf("#D4AF37", "#1E1710", "#0B0907"),
      tags = listOf("Arch", "Mosque", "Mihrab", "Gold"),
      isPremium = false,
      patternType = WallpaperPatternType.ISLAMIC_ARCH,
      primaryGradientStart = 0xFF24190C,
      primaryGradientEnd = 0xFF0A0703,
      accentColor = 0xFFF59E0B,
      glyphOrLetter = "محراب",
      subtextArabic = "محراب الجمال والنور الإلهي",
      subtextEnglish = "Portal of Divine Illumination",
      downloads = 4870,
      likes = 2210
    ),

    // Dark
    Wallpaper(
      id = "wall_dark_obsidian_gold",
      title = "Obsidian Silk & Liquid Gold",
      titleAr = "حرير أوبسيديان وذهب سائل",
      category = WallpaperCategory.DARK,
      colors = listOf("#D4AF37", "#14171F", "#07090C"),
      tags = listOf("Dark", "Obsidian", "Minimal", "GoldAccent"),
      isPremium = false,
      patternType = WallpaperPatternType.MINIMAL_DARK_MOSAIC,
      primaryGradientStart = 0xFF121620,
      primaryGradientEnd = 0xFF050608,
      accentColor = 0xFFD4AF37,
      glyphOrLetter = "نقاء",
      subtextArabic = "سواد نقي تتلألأ فيه خطوط الذهب",
      subtextEnglish = "Pure deep obsidian kissed with gold dust",
      downloads = 8900,
      likes = 4600
    ),
    Wallpaper(
      id = "wall_dark_kaaba_kiswah",
      title = "Kiswah Gold Weave Texture",
      titleAr = "نقش كسوة الكعبة المشرفة",
      category = WallpaperCategory.DARK,
      colors = listOf("#D4AF37", "#181410", "#080705"),
      tags = listOf("Kiswah", "Kaaba", "Sacred", "Mecca"),
      isPremium = true,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF15100B,
      primaryGradientEnd = 0xFF060503,
      accentColor = 0xFFD4AF37,
      glyphOrLetter = "يَا حَيُّ يَا قَيُّومُ",
      subtextArabic = "يَا حَيُّ يَا قَيُّومُ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
      subtextEnglish = "O Ever-Living, O Self-Sustaining, Lord of Majesty",
      downloads = 12400,
      likes = 6800
    ),
    Wallpaper(
      id = "wall_dark_kufic_matrix",
      title = "Midnight Kufic Geometry",
      titleAr = "هندسة كوفية ليلية",
      category = WallpaperCategory.DARK,
      colors = listOf("#64748B", "#0F172A", "#020617"),
      tags = listOf("Kufic", "Dark", "Minimal", "Architecture"),
      isPremium = false,
      patternType = WallpaperPatternType.MINIMAL_DARK_MOSAIC,
      primaryGradientStart = 0xFF0F172A,
      primaryGradientEnd = 0xFF020617,
      accentColor = 0xFF94A3B8,
      glyphOrLetter = "توحيد",
      subtextArabic = "خط كوفي مربع على سواد الفضاء",
      subtextEnglish = "Square Kufic harmony on dark canvas",
      downloads = 3450,
      likes = 1320
    ),

    // Calligraphy
    Wallpaper(
      id = "wall_callig_allah",
      title = "The Name of Allah (الله)",
      titleAr = "لفظ الجلالة (الله) بنور الذهب",
      category = WallpaperCategory.CALLIGRAPHY,
      colors = listOf("#D4AF37", "#1A150D", "#0A0805"),
      tags = listOf("Allah", "Divine", "Thuluth", "Masterpiece"),
      isPremium = false,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF241B0D,
      primaryGradientEnd = 0xFF0A0704,
      accentColor = 0xFFFCD34D,
      glyphOrLetter = "اللّه",
      subtextArabic = "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ",
      subtextEnglish = "Allah is the Light of the heavens and earth",
      downloads = 14200,
      likes = 7890
    ),
    Wallpaper(
      id = "wall_callig_muhammad",
      title = "Prophet Muhammad (محمد)",
      titleAr = "اسم الحبيب محمد ﷺ بالذهب",
      category = WallpaperCategory.CALLIGRAPHY,
      colors = listOf("#10B981", "#0B2117", "#030C08"),
      tags = listOf("Muhammad", "Prophet", "Emerald", "Peace"),
      isPremium = false,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF0D2A1D,
      primaryGradientEnd = 0xFF040F0A,
      accentColor = 0xFF34D399,
      glyphOrLetter = "مُحَمَّد",
      subtextArabic = "صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ",
      subtextEnglish = "Peace and Blessings be upon him",
      downloads = 11300,
      likes = 5980
    ),
    Wallpaper(
      id = "wall_callig_bismillah",
      title = "Bismillah Al-Rahman",
      titleAr = "بسم الله الرحمن الرحيم بخط الثلث",
      category = WallpaperCategory.CALLIGRAPHY,
      colors = listOf("#D4AF37", "#1E1A29", "#0B0912"),
      tags = listOf("Bismillah", "Grace", "Calligraphy", "Blessing"),
      isPremium = true,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF201730,
      primaryGradientEnd = 0xFF090610,
      accentColor = 0xFFF6E27A,
      glyphOrLetter = "بِسْمِ اللَّهِ",
      subtextArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
      subtextEnglish = "In the name of Allah, the Most Gracious, Most Merciful",
      downloads = 9980,
      likes = 5120
    ),
    Wallpaper(
      id = "wall_callig_al_salam",
      title = "Al-Salam (Peace)",
      titleAr = "اسم الله السلام",
      category = WallpaperCategory.CALLIGRAPHY,
      colors = listOf("#2563EB", "#0C172E", "#040812"),
      tags = listOf("Peace", "Salam", "Sapphire", "Tranquility"),
      isPremium = false,
      patternType = WallpaperPatternType.CALLIGRAPHY_GLYPH,
      primaryGradientStart = 0xFF0F1E3D,
      primaryGradientEnd = 0xFF040812,
      accentColor = 0xFF60A5FA,
      glyphOrLetter = "السَّلَام",
      subtextArabic = "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ الْمَلِكُ الْقُدُّوسُ السَّلَامُ",
      subtextEnglish = "He is Allah, the King, the Holy, the Source of Peace",
      downloads = 6700,
      likes = 3400
    )
  )

  val curatedQuotePresets: List<QuotePreset> = listOf(
    QuotePreset(
      id = "q_1",
      arabicText = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
      englishText = "Unquestionably, by the remembrance of Allah hearts are assured.",
      sourceAr = "سورة الرعد: 28",
      sourceEn = "Surah Ar-Ra'd: 28",
      category = "Quran"
    ),
    QuotePreset(
      id = "q_2",
      arabicText = "وَقُل رَّبِّ زِدْنِي عِلْمًا",
      englishText = "And say, 'My Lord, increase me in knowledge.'",
      sourceAr = "سورة طه: 114",
      sourceEn = "Surah Ta-Ha: 114",
      category = "Quran"
    ),
    QuotePreset(
      id = "q_3",
      arabicText = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا • إِنَّ مَعَ الْعُسْرِ يُسْرًا",
      englishText = "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.",
      sourceAr = "سورة الشرح: 5-6",
      sourceEn = "Surah Ash-Sharh: 5-6",
      category = "Quran"
    ),
    QuotePreset(
      id = "q_4",
      arabicText = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
      englishText = "Our Lord, give us in this world good and in the Hereafter good.",
      sourceAr = "سورة البقرة: 201",
      sourceEn = "Surah Al-Baqarah: 201",
      category = "Dua"
    ),
    QuotePreset(
      id = "q_5",
      arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
      englishText = "Sufficient for us is Allah, and [He is] the best Disposer of affairs.",
      sourceAr = "سورة آل عمران: 173",
      sourceEn = "Surah Ali 'Imran: 173",
      category = "Quran"
    ),
    QuotePreset(
      id = "q_6",
      arabicText = "إِنَّ اللَّهَ مَعَ الصَّابِرِينَ",
      englishText = "Indeed, Allah is with the patient.",
      sourceAr = "سورة البقرة: 153",
      sourceEn = "Surah Al-Baqarah: 153",
      category = "Quran"
    ),
    QuotePreset(
      id = "q_7",
      arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
      englishText = "The best among you is he who learns the Quran and teaches it.",
      sourceAr = "صحيح البخاري",
      sourceEn = "Sahih Al-Bukhari",
      category = "Hadith"
    ),
    QuotePreset(
      id = "q_8",
      arabicText = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
      englishText = "Smiling in the face of your brother is an act of charity.",
      sourceAr = "جامع الترمذي",
      sourceEn = "Jami' At-Tirmidhi",
      category = "Hadith"
    )
  )

  val premiumPlans: List<PremiumPlan> = listOf(
    PremiumPlan(
      duration = PlanDuration.MONTHLY,
      priceAr = "11.99 ر.س / شهرياً",
      priceEn = "$2.99 / month",
      titleAr = "الاشتراك الشهري",
      titleEn = "Monthly Plan",
      isPopular = false
    ),
    PremiumPlan(
      duration = PlanDuration.YEARLY,
      priceAr = "74.99 ر.س / سنوياً",
      priceEn = "$19.99 / year",
      titleAr = "الاشتراك السنوي",
      titleEn = "Yearly Plan",
      tagAr = "وفر 45% • الأفضل قيمة",
      tagEn = "Save 45% • Best Value",
      isPopular = true
    ),
    PremiumPlan(
      duration = PlanDuration.LIFETIME,
      priceAr = "189.99 ر.س لمرة واحدة",
      priceEn = "$49.99 one-time",
      titleAr = "مدى الحياة",
      titleEn = "Lifetime Access",
      tagAr = "امتلاك دائم",
      tagEn = "Forever Yours",
      isPopular = false
    )
  )
}

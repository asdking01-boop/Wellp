package com.example.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(val code: String, val displayName: String, val layoutDirection: LayoutDirection) {
  ARABIC("ar", "العربية", LayoutDirection.Rtl),
  ENGLISH("en", "English", LayoutDirection.Ltr)
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.ARABIC }

object Strings {
  private val translations = mapOf(
    // App Header & Navigation
    "app_name" to mapOf(AppLanguage.ARABIC to "خلفيات عربية", AppLanguage.ENGLISH to "ArabicWalls"),
    "home" to mapOf(AppLanguage.ARABIC to "الرئيسية", AppLanguage.ENGLISH to "Home"),
    "favorites" to mapOf(AppLanguage.ARABIC to "المفضلة", AppLanguage.ENGLISH to "Favorites"),
    "quote_editor" to mapOf(AppLanguage.ARABIC to "محرر الاقتباسات", AppLanguage.ENGLISH to "Quote Editor"),
    "premium" to mapOf(AppLanguage.ARABIC to "النسخة المميزة", AppLanguage.ENGLISH to "Premium"),
    "admin_generator" to mapOf(AppLanguage.ARABIC to "توليد ذكي", AppLanguage.ENGLISH to "Admin AI"),
    "settings" to mapOf(AppLanguage.ARABIC to "الإعدادات", AppLanguage.ENGLISH to "Settings"),

    // Categories
    "cat_all" to mapOf(AppLanguage.ARABIC to "الكل", AppLanguage.ENGLISH to "All"),
    "cat_arabic_letters" to mapOf(AppLanguage.ARABIC to "حروف عربية", AppLanguage.ENGLISH to "Arabic Letters"),
    "cat_nature" to mapOf(AppLanguage.ARABIC to "طبيعة", AppLanguage.ENGLISH to "Nature"),
    "cat_occasions" to mapOf(AppLanguage.ARABIC to "مناسبات", AppLanguage.ENGLISH to "Occasions"),
    "cat_geometric" to mapOf(AppLanguage.ARABIC to "زخارف هندسية", AppLanguage.ENGLISH to "Geometric"),
    "cat_dark" to mapOf(AppLanguage.ARABIC to "داكنة", AppLanguage.ENGLISH to "Dark"),
    "cat_calligraphy" to mapOf(AppLanguage.ARABIC to "خط عربي", AppLanguage.ENGLISH to "Calligraphy"),
    "cat_ramadan" to mapOf(AppLanguage.ARABIC to "رمضان", AppLanguage.ENGLISH to "Ramadan"),
    "cat_eid" to mapOf(AppLanguage.ARABIC to "العيد", AppLanguage.ENGLISH to "Eid"),

    // Color Filters
    "filter_colors" to mapOf(AppLanguage.ARABIC to "تصفية حسب اللون", AppLanguage.ENGLISH to "Filter by Color"),
    "color_all" to mapOf(AppLanguage.ARABIC to "جميع الألوان", AppLanguage.ENGLISH to "All Colors"),
    "color_gold" to mapOf(AppLanguage.ARABIC to "ذهبي", AppLanguage.ENGLISH to "Gold"),
    "color_emerald" to mapOf(AppLanguage.ARABIC to "زمردي", AppLanguage.ENGLISH to "Emerald"),
    "color_sapphire" to mapOf(AppLanguage.ARABIC to "أزرق ملكي", AppLanguage.ENGLISH to "Sapphire"),
    "color_ruby" to mapOf(AppLanguage.ARABIC to "ياقوتي", AppLanguage.ENGLISH to "Ruby"),
    "color_charcoal" to mapOf(AppLanguage.ARABIC to "فحمي", AppLanguage.ENGLISH to "Charcoal"),
    "color_amethyst" to mapOf(AppLanguage.ARABIC to "بنفسجي", AppLanguage.ENGLISH to "Amethyst"),
    "color_amber" to mapOf(AppLanguage.ARABIC to "عنبري", AppLanguage.ENGLISH to "Amber"),

    // Home Screen
    "search_hint" to mapOf(AppLanguage.ARABIC to "ابحث في الخلفيات الإسلامية والعربية...", AppLanguage.ENGLISH to "Search Arabic & Islamic wallpapers..."),
    "premium_badge" to mapOf(AppLanguage.ARABIC to "مميز", AppLanguage.ENGLISH to "PRO"),
    "infinite_loading" to mapOf(AppLanguage.ARABIC to "جاري تحميل المزيد من الخلفيات...", AppLanguage.ENGLISH to "Loading more wallpapers..."),
    "no_wallpapers_found" to mapOf(AppLanguage.ARABIC to "لم يتم العثور على خلفيات مطابقة", AppLanguage.ENGLISH to "No matching wallpapers found"),
    "reset_filters" to mapOf(AppLanguage.ARABIC to "إعادة ضبط التصفية", AppLanguage.ENGLISH to "Reset Filters"),

    // Preview Screen
    "set_as_wallpaper" to mapOf(AppLanguage.ARABIC to "تعيين كخلفية", AppLanguage.ENGLISH to "Set as Wallpaper"),
    "download" to mapOf(AppLanguage.ARABIC to "تحميل", AppLanguage.ENGLISH to "Download"),
    "favorite" to mapOf(AppLanguage.ARABIC to "إضافة للمفضلة", AppLanguage.ENGLISH to "Favorite"),
    "unfavorite" to mapOf(AppLanguage.ARABIC to "إزالة من المفضلة", AppLanguage.ENGLISH to "Remove Favorite"),
    "edit_quote" to mapOf(AppLanguage.ARABIC to "إضافة اقتباس", AppLanguage.ENGLISH to "Add Quote"),
    "preview_lockscreen" to mapOf(AppLanguage.ARABIC to "معاينة القفل", AppLanguage.ENGLISH to "Lock Screen Preview"),
    "wallpaper_info" to mapOf(AppLanguage.ARABIC to "معلومات الخلفية", AppLanguage.ENGLISH to "Wallpaper Info"),
    "downloads_count" to mapOf(AppLanguage.ARABIC to "التحميلات", AppLanguage.ENGLISH to "Downloads"),
    "resolution_4k" to mapOf(AppLanguage.ARABIC to "الدقة: 4K فائقة الوضوح", AppLanguage.ENGLISH to "Resolution: 4K Ultra HD"),
    "artist" to mapOf(AppLanguage.ARABIC to "الفنان المصمم", AppLanguage.ENGLISH to "Designer / Artist"),

    // Set Wallpaper Dialog
    "choose_wallpaper_target" to mapOf(AppLanguage.ARABIC to "اختر مكان تطبيق الخلفية", AppLanguage.ENGLISH to "Set Wallpaper Location"),
    "target_home" to mapOf(AppLanguage.ARABIC to "الشاشة الرئيسية", AppLanguage.ENGLISH to "Home Screen"),
    "target_lock" to mapOf(AppLanguage.ARABIC to "شاشة القفل", AppLanguage.ENGLISH to "Lock Screen"),
    "target_both" to mapOf(AppLanguage.ARABIC to "الشاشة الرئيسية وشاشة القفل", AppLanguage.ENGLISH to "Both Screens"),
    "cancel" to mapOf(AppLanguage.ARABIC to "إلغاء", AppLanguage.ENGLISH to "Cancel"),
    "wallpaper_applied_success" to mapOf(AppLanguage.ARABIC to "تم تعيين الخلفية بنجاح!", AppLanguage.ENGLISH to "Wallpaper applied successfully!"),
    "download_success" to mapOf(AppLanguage.ARABIC to "تم حفظ الخلفية في ألبوم الصور!", AppLanguage.ENGLISH to "Wallpaper saved to gallery!"),

    // Quote Editor
    "editor_title" to mapOf(AppLanguage.ARABIC to "محرر الاقتباس والآيات", AppLanguage.ENGLISH to "Quote & Verse Editor"),
    "enter_custom_text" to mapOf(AppLanguage.ARABIC to "اكتب آية، حديثاً، أو بيتاً شعرياً...", AppLanguage.ENGLISH to "Write an ayah, hadith, or quote..."),
    "preset_quotes" to mapOf(AppLanguage.ARABIC to "نماذج مختارة", AppLanguage.ENGLISH to "Curated Presets"),
    "font_family" to mapOf(AppLanguage.ARABIC to "نوع الخط", AppLanguage.ENGLISH to "Font Family"),
    "font_cairo" to mapOf(AppLanguage.ARABIC to "خط كايرو (Cairo)", AppLanguage.ENGLISH to "Cairo Font"),
    "font_amiri" to mapOf(AppLanguage.ARABIC to "خط أميري كلاسيكي (Amiri)", AppLanguage.ENGLISH to "Amiri Classic"),
    "font_size" to mapOf(AppLanguage.ARABIC to "حجم الخط", AppLanguage.ENGLISH to "Font Size"),
    "text_color" to mapOf(AppLanguage.ARABIC to "لون النص", AppLanguage.ENGLISH to "Text Color"),
    "position" to mapOf(AppLanguage.ARABIC to "المحاذاة والموقع", AppLanguage.ENGLISH to "Alignment & Position"),
    "pos_top" to mapOf(AppLanguage.ARABIC to "أعلى", AppLanguage.ENGLISH to "Top"),
    "pos_center" to mapOf(AppLanguage.ARABIC to "وسط", AppLanguage.ENGLISH to "Center"),
    "pos_bottom" to mapOf(AppLanguage.ARABIC to "أسفل", AppLanguage.ENGLISH to "Bottom"),
    "bg_dim_blur" to mapOf(AppLanguage.ARABIC to "تعتيم وضبابية الخلفية للنص", AppLanguage.ENGLISH to "Background Dim & Soft Overlay"),
    "save_artwork" to mapOf(AppLanguage.ARABIC to "حفظ التصميم", AppLanguage.ENGLISH to "Save Artwork"),
    "apply_as_wallpaper" to mapOf(AppLanguage.ARABIC to "تطبيق كخلفية مباشرة", AppLanguage.ENGLISH to "Apply Directly as Wallpaper"),
    "source_reference" to mapOf(AppLanguage.ARABIC to "المصدر / السورة (اختياري)", AppLanguage.ENGLISH to "Source / Surah (optional)"),

    // Favorites Screen
    "favorites_title" to mapOf(AppLanguage.ARABIC to "خلفياتك المفضلة", AppLanguage.ENGLISH to "Your Favorites"),
    "no_favorites_yet" to mapOf(AppLanguage.ARABIC to "لا توجد خلفيات في المفضلة بعد", AppLanguage.ENGLISH to "No favorites added yet"),
    "no_favorites_desc" to mapOf(AppLanguage.ARABIC to "اضغط على أيقونة القلب في أي خلفية لحفظها هنا والوصول إليها بسرعة.", AppLanguage.ENGLISH to "Tap the heart icon on any wallpaper to save it here for fast access."),
    "explore_wallpapers" to mapOf(AppLanguage.ARABIC to "تصفح الخلفيات الآن", AppLanguage.ENGLISH to "Explore Wallpapers"),

    // Premium Screen
    "premium_title" to mapOf(AppLanguage.ARABIC to "العضوية الذهبية الفاخرة", AppLanguage.ENGLISH to "Premium Gold Membership"),
    "premium_subtitle" to mapOf(AppLanguage.ARABIC to "افتح أندر مجموعات الخط العربي والزخارف بدقة 4K فائقة", AppLanguage.ENGLISH to "Unlock rare Arabic calligraphy & 4K Islamic patterns"),
    "plan_monthly" to mapOf(AppLanguage.ARABIC to "الاشتراك الشهري", AppLanguage.ENGLISH to "Monthly Plan"),
    "plan_monthly_price" to mapOf(AppLanguage.ARABIC to "11.99 ر.س / شهر", AppLanguage.ENGLISH to "$2.99 / month"),
    "plan_yearly" to mapOf(AppLanguage.ARABIC to "الاشتراك السنوي", AppLanguage.ENGLISH to "Yearly Plan"),
    "plan_yearly_price" to mapOf(AppLanguage.ARABIC to "74.99 ر.س / سنة", AppLanguage.ENGLISH to "$19.99 / year"),
    "plan_yearly_badge" to mapOf(AppLanguage.ARABIC to "الأكثر طلباً - وفر 45%", AppLanguage.ENGLISH to "Best Value - Save 45%"),
    "plan_lifetime" to mapOf(AppLanguage.ARABIC to "مدى الحياة", AppLanguage.ENGLISH to "Lifetime Access"),
    "plan_lifetime_price" to mapOf(AppLanguage.ARABIC to "189.99 ر.س دفعة واحدة", AppLanguage.ENGLISH to "$49.99 one-time"),
    "premium_feature_1" to mapOf(AppLanguage.ARABIC to "تحميل غير محدود لجميع الخلفيات بدقة 4K", AppLanguage.ENGLISH to "Unlimited downloads of all 4K wallpapers"),
    "premium_feature_2" to mapOf(AppLanguage.ARABIC to "مجموعات حصرية لمناسبات رمضان وعيدي الفطر والأضحى", AppLanguage.ENGLISH to "Exclusive Ramadan & Eid calligraphy series"),
    "premium_feature_3" to mapOf(AppLanguage.ARABIC to "تصدير محرر الاقتباسات بدون علامة مائية وبأعلى جودة", AppLanguage.ENGLISH to "Export quotes without watermark in full fidelity"),
    "premium_feature_4" to mapOf(AppLanguage.ARABIC to "تجربة نقية وفاخرة بالكامل بدون أي إعلانات", AppLanguage.ENGLISH to "100% Ad-free pristine experience"),
    "unlock_now" to mapOf(AppLanguage.ARABIC to "ترقية العضوية الآن", AppLanguage.ENGLISH to "Unlock Premium Now"),
    "restore_purchases" to mapOf(AppLanguage.ARABIC to "استعادة المشتريات", AppLanguage.ENGLISH to "Restore Purchases"),
    "premium_active" to mapOf(AppLanguage.ARABIC to "أنت مشترك في الباقة الذهبية", AppLanguage.ENGLISH to "Gold Member Active"),
    "demo_toggle" to mapOf(AppLanguage.ARABIC to "تفعيل النسخة المميزة (تجريبي)", AppLanguage.ENGLISH to "Activate Demo Premium"),

    // Admin Bulk Wallpaper Generator Screen
    "admin_title" to mapOf(AppLanguage.ARABIC to "لوحة الإدارة: مولّد الخلفيات بالجملة (AI)", AppLanguage.ENGLISH to "Admin: Bulk AI Wallpaper Generator"),
    "admin_subtitle" to mapOf(AppLanguage.ARABIC to "توليد مجموعات خلفيات بالذكاء الاصطناعي عبر وسيط Gemini الخلفي الآمن", AppLanguage.ENGLISH to "Generate wallpaper collections via Gemini AI Backend Proxy"),
    "select_category" to mapOf(AppLanguage.ARABIC to "اختر الفئة المستهدفة", AppLanguage.ENGLISH to "Select Target Category"),
    "select_quantity" to mapOf(AppLanguage.ARABIC to "اختر الكمية المطلوبة", AppLanguage.ENGLISH to "Select Batch Quantity"),
    "daily_limit_title" to mapOf(AppLanguage.ARABIC to "الحد اليومي للتوليد (للتحكم بالتكاليف)", AppLanguage.ENGLISH to "Daily Generation Limit (Cost Control)"),
    "daily_usage" to mapOf(AppLanguage.ARABIC to "الاستهلاك اليومي", AppLanguage.ENGLISH to "Used Today"),
    "backend_proxy_status" to mapOf(AppLanguage.ARABIC to "وسيط الخادم الآمن (Firebase AI Proxy + App Check): متصل", AppLanguage.ENGLISH to "Backend Proxy (Firebase AI + App Check): Active"),
    "prompt_engine_preview" to mapOf(AppLanguage.ARABIC to "قوالب الأوامر التلقائية (اللون • النمط • الموضوع • الإضاءة • الزخرفة)", AppLanguage.ENGLISH to "Dynamic Prompt Variables (Color • Style • Subject • Lighting • Pattern)"),
    "start_bulk_generation" to mapOf(AppLanguage.ARABIC to "بدء التوليد المتوازي (4 دفعات معاً)", AppLanguage.ENGLISH to "Start Parallel Generation (Batches of 4)"),
    "stop_generation" to mapOf(AppLanguage.ARABIC to "إيقاف التوليد", AppLanguage.ENGLISH to "Stop Generation"),
    "remove_duplicates" to mapOf(AppLanguage.ARABIC to "حذف المكرر والمتشابه", AppLanguage.ENGLISH to "Remove Duplicates & Near-Duplicates"),
    "export_zip" to mapOf(AppLanguage.ARABIC to "تصدير المعتمدة + JSON كملف ZIP", AppLanguage.ENGLISH to "Export Approved + JSON as ZIP"),
    "approve" to mapOf(AppLanguage.ARABIC to "اعتماد", AppLanguage.ENGLISH to "Approve"),
    "reject" to mapOf(AppLanguage.ARABIC to "رفض", AppLanguage.ENGLISH to "Reject"),
    "regenerate" to mapOf(AppLanguage.ARABIC to "إعادة توليد", AppLanguage.ENGLISH to "Regenerate"),
    "approve_all" to mapOf(AppLanguage.ARABIC to "اعتماد الكل", AppLanguage.ENGLISH to "Approve All Ready"),
    "review_grid_title" to mapOf(AppLanguage.ARABIC to "شبكة المراجعة والاعتماد", AppLanguage.ENGLISH to "Generated Wallpapers Review Grid"),
    "status_pending" to mapOf(AppLanguage.ARABIC to "في الانتظار...", AppLanguage.ENGLISH to "Queued..."),
    "status_generating" to mapOf(AppLanguage.ARABIC to "جاري التوليد (30-60 ثانية)...", AppLanguage.ENGLISH to "Generating (30-60s)..."),
    "status_approved" to mapOf(AppLanguage.ARABIC to "معتمد في التطبيق", AppLanguage.ENGLISH to "Approved & Live"),
    "status_rejected" to mapOf(AppLanguage.ARABIC to "مرفوض", AppLanguage.ENGLISH to "Rejected"),
    "status_duplicate" to mapOf(AppLanguage.ARABIC to "مكرر مستبعد", AppLanguage.ENGLISH to "Duplicate Removed"),
    "reset_daily_counter" to mapOf(AppLanguage.ARABIC to "تصفير العداد", AppLanguage.ENGLISH to "Reset Today")
  )

  @Composable
  fun get(key: String): String {
    val lang = LocalAppLanguage.current
    return translations[key]?.get(lang) ?: translations[key]?.get(AppLanguage.ARABIC) ?: key
  }

  fun get(key: String, language: AppLanguage): String {
    return translations[key]?.get(language) ?: translations[key]?.get(AppLanguage.ARABIC) ?: key
  }
}

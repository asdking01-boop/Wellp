package com.example.data

import com.example.model.BulkGenCategory
import com.example.model.PromptVariation
import kotlin.random.Random

object PromptTemplateEngine {

  const val MANDATORY_SUFFIX = "vertical phone wallpaper, 9:19.5, high resolution, no text, no watermark"

  data class BilingualToken(val en: String, val ar: String)

  private val colorVariables = listOf(
    BilingualToken("liquid 24k gold and deep obsidian black", "ذهب سائل وأسود أوبسيديان"),
    BilingualToken("emerald green and antique brass", "أخضر زمردي ونحاس عتيق"),
    BilingualToken("royal sapphire blue and shimmering silver", "أزرق ملكي وفضة متلألئة"),
    BilingualToken("crimson ruby red and warm champagne gold", "ياقوتي قرمزي وذهب دافئ"),
    BilingualToken("midnight indigo and rose gold", "كحلي منتصف الليل وذهب وردي"),
    BilingualToken("desert amber and matte charcoal", "عنبري صحراوي وفحمي داكن"),
    BilingualToken("turquoise teal and pearl white accents", "فيروزي وعاجي لؤلؤي"),
    BilingualToken("mystic amethyst purple and burnished bronze", "بنفسجي ملكي وبرونز مصقول")
  )

  private val styleVariables = listOf(
    BilingualToken("3D luxury relief sculpture", "نحت بارز ثلاثي الأبعاد فاخر"),
    BilingualToken("Ottoman palace miniature illumination", "تذهيب عثماني كلاسيكي"),
    BilingualToken("Andalusian architectural mosaic", "فسيفساء معمارية أندلسية"),
    BilingualToken("minimalist modern dark glassmorphism", "تصميم زجاجي داكن حديث"),
    BilingualToken("handcrafted Damascus metallic inlay", "تطعيم معدني دمشقي أصيل"),
    BilingualToken("velvet silk embroidery with gold thread", "تطريز حريري بخيوط الذهب"),
    BilingualToken("sacred geometric paper-cut shadowbox", "طبقات ورقية هندسية متداخلة"),
    BilingualToken("cinematic photorealistic macro render", "تصوير سينمائي فائق الدقة")
  )

  private val lightingVariables = listOf(
    BilingualToken("warm golden hour rim lighting", "إضاءة الساعة الذهبية الدافئة"),
    BilingualToken("soft celestial moonlight glow", "توهج ضوء القمر الهادئ"),
    BilingualToken("dramatic chiaroscuro studio shadows", "ظلال درامية متباينة"),
    BilingualToken("ethereal volumetric god rays", "أشعة نورانية روحانية"),
    BilingualToken("candlelit brass lantern ambiance", "أجواء فوانيس نحاسية مضيئة"),
    BilingualToken("starlit cosmic twilight", "شفق كوني مرصع بالنجوم"),
    BilingualToken("subtle neon gold subsurface scattering", "إشعاع ذهبي ناعم"),
    BilingualToken("crisp museum gallery spotlight", "إضاءة متحفية مركزة")
  )

  private val patternVariables = listOf(
    BilingualToken("twelve-pointed Moroccan Zellij star", "نجمة الزليج المغربي"),
    BilingualToken("interlocking Kufic labyrinth lattice", "شبكة هندسية كوفية متداخلة"),
    BilingualToken("arabesque floral vine scrollwork", "توريق نباتي إسلامي"),
    BilingualToken("sacred Muqarnas honeycomb vaulting", "مقرنصات إسلامية متناغمة"),
    BilingualToken("concentric mandalas of Rub el Hizb", "دوائر ربع الحزب المتمركزة"),
    BilingualToken("delicate mashrabiya wooden latticework", "مشربية خشبية دقيقة"),
    BilingualToken("flowing golden silk ribbon waves", "تموجات حريرية ذهبية"),
    BilingualToken("hexagonal Islamic tessellation", "فسيفساء سداسية متناسقة")
  )

  private val subjectsByCategory: Map<BulkGenCategory, List<BilingualToken>> = mapOf(
    BulkGenCategory.ARABIC_LETTERS to listOf(
      BilingualToken("abstract sculptural curve inspired by Arabic letter Waw", "انحناءة حرف الواو الفنية"),
      BilingualToken("flowing calligraphic stroke of letter Noon with ink droplets", "انسيابية حرف النون"),
      BilingualToken("majestic vertical silhouette of Arabic letter Alif", "شموخ حرف الألف"),
      BilingualToken("graceful spiral ribbon inspired by Thuluth letter Ha", "جمالية حرف الحاء بالثلث"),
      BilingualToken("intertwined Diwani calligraphic flourishes in abstract form", "حروف ديوانية متداخلة"),
      BilingualToken("monumental Kufic geometric block sculpture", "تكوين حروفي كوفي")
    ),
    BulkGenCategory.NATURE to listOf(
      BilingualToken("sweeping Arabian desert sand dunes under a crescent moon", "كثبان الصحراء العربية تحت الهلال"),
      BilingualToken("serene emerald oasis with date palm silhouettes reflected in water", "واحة نخيل زمردية هادئة"),
      BilingualToken("misty Atlas mountain peaks at dawn with cedar trees", "قمم الجبال عند الفجر"),
      BilingualToken("tranquil courtyard fountain surrounded by jasmine and olive trees", "نافورة فناء أندلسي وأشجار الزيتون"),
      BilingualToken("starry night sky over ancient desert canyon", "سماء الليل المرصعة بالنجوم فوق الوادي"),
      BilingualToken("blooming damask roses with morning dew drops", "ورود دمشقية ندية")
    ),
    BulkGenCategory.OCCASIONS to listOf(
      BilingualToken("festive Islamic celebration archway with hanging brass ornaments", "قوس احتفالي إسلامي مزين"),
      BilingualToken("illuminated marble mosque courtyard during a blessed night", "ساحة مسجد رخامية مضيئة"),
      BilingualToken("celestial night of decree sky with glowing crescent and stars", "إشراقة ليلة مباركة بالنجوم"),
      BilingualToken("ornate incense burner (Mabkhara) with fragrant rising smoke curls", "مبخرة تراثية فاخرة"),
      BilingualToken("ceremonial golden astrolabe and celestial spheres", "إسطرلاب ذهبي فلكي"),
      BilingualToken("sacred mihrab portal bathed in peaceful light", "محراب مشرق بنور السكينة")
    ),
    BulkGenCategory.GEOMETRIC to listOf(
      BilingualToken("complex 16-fold Islamic star rosette centerpiece", "نجمة إسلامية ذات ١٦ رأساً"),
      BilingualToken("infinite fractal tessellation of Alhambra palace tiles", "زخارف قصر الحمراء اللانهائية"),
      BilingualToken("layered golden brass astrolabe rings in 3D space", "حلقات هندسية ذهبية ثلاثية الأبعاد"),
      BilingualToken("symmetrical Mamluk marble inlay medallion", "مدالية رخامية مملوكية متناظرة"),
      BilingualToken("architectural dome interior with intricate geometric ribs", "قبة معمارية مزخرفة"),
      BilingualToken("sacred geometry polyhedral crystal lattice", "بلورات الهندسة الإسلامية المقدسة")
    ),
    BulkGenCategory.DARK to listOf(
      BilingualToken("ultra-dark matte obsidian monolith with hairline gold veins", "حجر أوبسيديان داكن بعروق الذهب"),
      BilingualToken("shadowy black silk Kiswah weave texture with subtle gold embroidery", "نسيج حريري أسود مطرز بالذهب"),
      BilingualToken("minimalist pitch-black abyss with a single glowing golden crescent", "سواد الليل مع هلال ذهبي متوهج"),
      BilingualToken("dark carbon fiber Islamic geometric relief in low-key lighting", "نقش هندسي داكن بإضاءة خافتة"),
      BilingualToken("midnight black marble surface with liquid gold kintsugi cracks", "رخام أسود مع شذرات الذهب السائل"),
      BilingualToken("deep eclipse silhouette with a thin golden corona ring", "كسوف مهيب بحلقة ذهبية")
    ),
    BulkGenCategory.RAMADAN to listOf(
      BilingualToken("traditional glowing Ramadan Fanous lantern hanging from an arch", "فانوس رمضان التقليدي المضيء"),
      BilingualToken("slender golden Ramadan crescent moon nestled in starry clouds", "هلال رمضان الذهبي بين الغيوم"),
      BilingualToken("ornate brass cannon and lantern overlooking a twilight skyline", "أجواء مدفع وفانوس الإفطار"),
      BilingualToken("cluster of stained-glass Moroccan Ramadan lamps glowing warmly", "مصابيح رمضانية ملونة دافئة"),
      BilingualToken("peaceful Suhoor night sky with glowing minarets in the distance", "ليالي السحور والمآذن المضيئة"),
      BilingualToken("golden dates and brass coffee dallah in a serene Ramadan alcove", "جلسة رمضانية تراثية هادئة")
    ),
    BulkGenCategory.EID to listOf(
      BilingualToken("joyful Eid celebration scene with glowing golden lanterns and stars", "بهجة العيد بالفوانيس الذهبية"),
      BilingualToken("luxurious festive Eid gift boxes wrapped in silk and gold filigree", "هدايا العيد الفاخرة بالحرير والذهب"),
      BilingualToken("illuminated grand mosque domes under celebratory night sky", "قباب المسجد المشرقة في ليلة العيد"),
      BilingualToken("ornate golden hospitality tray with blooming white flowers", "ضيافة العيد الذهبية المميزة"),
      BilingualToken("radiant golden sunbeams breaking through an Andalusian arch for Eid morning", "إشراقة صباح العيد من قوس أندلسي"),
      BilingualToken("sparkling golden crescent and eight-pointed stars in festive harmony", "تناغم هلال ونجوم العيد المبهجة")
    )
  )

  private val promptTemplates: Map<BulkGenCategory, List<String>> = mapOf(
    BulkGenCategory.ARABIC_LETTERS to listOf(
      "An artistic masterpiece of {subject}, crafted in {style} with {color} palette, surrounded by {pattern}, illuminated by {lighting}, {suffix}",
      "Breathtaking {style} featuring {subject}, adorned with {pattern} in {color}, bathed in {lighting}, {suffix}",
      "Luxury Islamic art composition of {subject}, rendered as {style}, rich {color} tones, intricate {pattern} background, {lighting}, {suffix}"
    ),
    BulkGenCategory.NATURE to listOf(
      "Majestic landscape of {subject}, interpreted in {style} with {color} hues, subtle {pattern} border framing, {lighting}, {suffix}",
      "Serene natural scene showing {subject}, {lighting}, rich {color} atmosphere, hints of {pattern} in the composition, {style}, {suffix}",
      "Atmospheric view of {subject} under {lighting}, styled as {style} with {color} palette and {pattern} harmony, {suffix}"
    ),
    BulkGenCategory.OCCASIONS to listOf(
      "Sacred Islamic occasion artwork featuring {subject}, designed in {style} with {color}, intricate {pattern} details, {lighting}, {suffix}",
      "Celebratory composition of {subject}, {lighting}, crafted with {style} and {pattern} motifs in {color}, {suffix}"
    ),
    BulkGenCategory.GEOMETRIC to listOf(
      "Hypnotic sacred Islamic geometry showcasing {subject} and {pattern}, executed in {style} with {color}, {lighting}, {suffix}",
      "Symmetrical architectural masterpiece of {subject}, interwoven with {pattern}, {color} finish, {style}, {lighting}, {suffix}"
    ),
    BulkGenCategory.DARK to listOf(
      "AMOLED dark luxury composition of {subject}, {style}, deep {color}, subtle {pattern} etching, {lighting}, {suffix}",
      "Minimalist nocturnal artwork featuring {subject}, {lighting}, {color} accents over {pattern}, {style}, {suffix}"
    ),
    BulkGenCategory.RAMADAN to listOf(
      "Spiritual Ramadan artwork depicting {subject}, framed by {pattern} in {style}, glowing with {lighting} and {color}, {suffix}",
      "Blessed Ramadan atmosphere featuring {subject}, {color} palette, {style} craftsmanship with {pattern}, {lighting}, {suffix}"
    ),
    BulkGenCategory.EID to listOf(
      "Festive and royal Eid artwork of {subject}, {style} with {pattern} embellishments, radiant {lighting} in {color}, {suffix}",
      "Joyful Eid celebration masterpiece showing {subject}, {color} tones, {pattern} background, {style}, {lighting}, {suffix}"
    )
  )

  /**
   * Generates `count` unique prompt combinations for the given category so no two prompts are alike.
   */
  fun generateUniquePrompts(
    category: BulkGenCategory,
    count: Int
  ): List<PromptVariation> {
    val subjects = subjectsByCategory[category] ?: subjectsByCategory.values.flatten()
    val templates = promptTemplates[category] ?: promptTemplates.values.flatten()
    val usedPrompts = mutableSetOf<String>()
    val result = mutableListOf<PromptVariation>()

    var attempt = 0
    while (result.size < count && attempt < count * 20) {
      attempt++
      val subj = subjects[(result.size + attempt) % subjects.size]
      val color = colorVariables[(result.size * 3 + attempt) % colorVariables.size]
      val style = styleVariables[(result.size * 5 + attempt) % styleVariables.size]
      val light = lightingVariables[(result.size * 7 + attempt) % lightingVariables.size]
      val pattern = patternVariables[(result.size * 11 + attempt) % patternVariables.size]
      val template = templates[(result.size + attempt) % templates.size]

      val builtPrompt = template
        .replace("{subject}", subj.en)
        .replace("{color}", color.en)
        .replace("{style}", style.en)
        .replace("{lighting}", light.en)
        .replace("{pattern}", pattern.en)
        .replace("{suffix}", MANDATORY_SUFFIX)

      // Ensure every prompt ends with the exact mandatory suffix
      val finalPrompt = if (builtPrompt.endsWith(MANDATORY_SUFFIX)) {
        builtPrompt
      } else {
        "$builtPrompt, $MANDATORY_SUFFIX"
      }

      if (usedPrompts.add(finalPrompt)) {
        result.add(
          PromptVariation(
            fullPrompt = finalPrompt,
            colorVar = color.en,
            styleVar = style.en,
            subjectVar = subj.en,
            lightingVar = light.en,
            patternVar = pattern.en,
            subjectAr = subj.ar,
            styleAr = style.ar,
            colorAr = color.ar
          )
        )
      }
    }

    return result
  }

  /**
   * Generates a single new unique prompt variation for regenerating a single slot.
   */
  fun generateSingleReplacementPrompt(
    category: BulkGenCategory,
    excludePrompts: Set<String>
  ): PromptVariation {
    val batch = generateUniquePrompts(category, excludePrompts.size + 5)
    return batch.firstOrNull { !excludePrompts.contains(it.fullPrompt) } ?: batch.last()
  }
}

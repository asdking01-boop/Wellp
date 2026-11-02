package com.example

import com.example.data.PromptTemplateEngine
import com.example.data.WallpaperData
import com.example.model.BulkGenCategory
import com.example.model.WallpaperCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun wallpaperData_hasValidEntries() {
    val wallpapers = WallpaperData.sampleWallpapers
    assertTrue("Wallpapers should not be empty", wallpapers.isNotEmpty())

    wallpapers.forEach { wall ->
      assertNotNull("ID must not be null", wall.id)
      assertTrue("Title Ar must not be blank", wall.titleAr.isNotBlank())
      assertTrue("Title En must not be blank", wall.title.isNotBlank())
      assertTrue("Colors must have at least one color", wall.colors.isNotEmpty())
    }
  }

  @Test
  fun wallpaperCategories_coverage() {
    val wallpapers = WallpaperData.sampleWallpapers
    val categories = wallpapers.map { it.category }.toSet()

    assertTrue("Must cover Arabic Letters", categories.contains(WallpaperCategory.ARABIC_LETTERS))
    assertTrue("Must cover Nature", categories.contains(WallpaperCategory.NATURE))
    assertTrue("Must cover Occasions", categories.contains(WallpaperCategory.OCCASIONS))
    assertTrue("Must cover Geometric", categories.contains(WallpaperCategory.GEOMETRIC))
    assertTrue("Must cover Dark", categories.contains(WallpaperCategory.DARK))
    assertTrue("Must cover Calligraphy", categories.contains(WallpaperCategory.CALLIGRAPHY))
  }

  @Test
  fun bulkPromptTemplateEngine_generatesUniquePromptsWithMandatorySuffix() {
    val suffix = "vertical phone wallpaper, 9:19.5, high resolution, no text, no watermark"
    for (cat in BulkGenCategory.entries) {
      val prompts = PromptTemplateEngine.generateUniquePrompts(cat, 25)
      assertEquals("Must generate exact requested quantity", 25, prompts.size)
      val uniqueTexts = prompts.map { it.fullPrompt }.toSet()
      assertEquals("Every prompt in batch must be unique", 25, uniqueTexts.size)
      prompts.forEach { p ->
        assertTrue(
          "Every prompt must end with mandatory suffix",
          p.fullPrompt.endsWith(suffix)
        )
      }
    }
  }

  @Test
  fun quotePresets_validity() {
    val presets = WallpaperData.curatedQuotePresets
    assertTrue(presets.isNotEmpty())
    presets.forEach { preset ->
      assertTrue(preset.arabicText.isNotBlank())
      assertTrue(preset.sourceAr.isNotBlank())
    }
  }

  @Test
  fun premiumPlans_coverage() {
    val plans = WallpaperData.premiumPlans
    assertEquals(3, plans.size)
  }
}

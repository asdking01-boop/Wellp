package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.WallpaperData
import com.example.util.ImageAnalysisAndExportUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ArabicWalls", appName)
  }

  @Test
  fun `jsonSerialization contains required fields`() {
    val json = ImageAnalysisAndExportUtil.serializeWallpapersToJson(WallpaperData.sampleWallpapers.take(2))
    assertTrue(json.contains("\"id\""))
    assertTrue(json.contains("\"title_ar\""))
    assertTrue(json.contains("\"title_en\""))
    assertTrue(json.contains("\"category\""))
    assertTrue(json.contains("\"colors\""))
    assertTrue(json.contains("\"tags\""))
    assertTrue(json.contains("\"isPremium\""))
    assertTrue(json.contains("\"imageUrl\""))
  }
}

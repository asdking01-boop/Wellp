package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.model.BulkGenCategory
import com.example.model.GeneratedWallpaperCandidate
import com.example.model.PromptVariation
import com.example.model.Wallpaper
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

object ImageAnalysisAndExportUtil {

  /**
   * Extracts up to 3 dominant hex color codes (#RRGGBB) from a Bitmap by sampling pixels
   * and clustering them into distinct color buckets.
   */
  fun extractDominantColors(bitmap: Bitmap, count: Int = 3): List<String> {
    val scaled = Bitmap.createScaledBitmap(bitmap, 32, 32, true)
    val bucketCounts = mutableMapOf<Int, Int>()
    val bucketR = mutableMapOf<Int, Long>()
    val bucketG = mutableMapOf<Int, Long>()
    val bucketB = mutableMapOf<Int, Long>()

    for (y in 0 until scaled.height) {
      for (x in 0 until scaled.width) {
        val pixel = scaled.getPixel(x, y)
        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)

        // Quantize into 4-bit per channel buckets (4096 buckets)
        val qR = r shr 5
        val qG = g shr 5
        val qB = b shr 5
        val key = (qR shl 6) or (qG shl 3) or qB

        bucketCounts[key] = (bucketCounts[key] ?: 0) + 1
        bucketR[key] = (bucketR[key] ?: 0L) + r
        bucketG[key] = (bucketG[key] ?: 0L) + g
        bucketB[key] = (bucketB[key] ?: 0L) + b
      }
    }

    val sortedBuckets = bucketCounts.entries.sortedByDescending { it.value }
    val selectedHexes = mutableListOf<String>()
    val selectedRgb = mutableListOf<Triple<Int, Int, Int>>()

    for (entry in sortedBuckets) {
      if (selectedHexes.size >= count) break
      val k = entry.key
      val c = entry.value.coerceAtLeast(1)
      val avgR = (bucketR[k]!! / c).toInt().coerceIn(0, 255)
      val avgG = (bucketG[k]!! / c).toInt().coerceIn(0, 255)
      val avgB = (bucketB[k]!! / c).toInt().coerceIn(0, 255)

      // Ensure color diversity between picked colors
      val isTooClose = selectedRgb.any { (er, eg, eb) ->
        val dist = sqrt(
          ((avgR - er) * (avgR - er) + (avgG - eg) * (avgG - eg) + (avgB - eb) * (avgB - eb)).toDouble()
        )
        dist < 38.0
      }

      if (!isTooClose || selectedHexes.isEmpty()) {
        selectedRgb.add(Triple(avgR, avgG, avgB))
        selectedHexes.add(String.format("#%02X%02X%02X", avgR, avgG, avgB))
      }
    }

    if (selectedHexes.isEmpty()) {
      selectedHexes.add("#D4AF37")
      selectedHexes.add("#090D14")
    }
    return selectedHexes
  }

  /**
   * Computes a 64-bit Difference Hash (dHash) perceptual fingerprint from a Bitmap
   * to detect duplicates and near-duplicates.
   */
  fun computePerceptualHash(bitmap: Bitmap): Long {
    // Resize to 9x8 so each of 8 rows has 8 horizontal adjacent pixel pairs
    val small = Bitmap.createScaledBitmap(bitmap, 9, 8, true)
    var hash = 0L
    var bitIndex = 0
    for (y in 0 until 8) {
      for (x in 0 until 8) {
        val leftPixel = small.getPixel(x, y)
        val rightPixel = small.getPixel(x + 1, y)
        val leftLum = Color.red(leftPixel) * 0.299 + Color.green(leftPixel) * 0.587 + Color.blue(leftPixel) * 0.114
        val rightLum = Color.red(rightPixel) * 0.299 + Color.green(rightPixel) * 0.587 + Color.blue(rightPixel) * 0.114
        if (leftLum > rightLum) {
          hash = hash or (1L shl bitIndex)
        }
        bitIndex++
      }
    }
    return hash
  }

  /**
   * Returns Hamming distance between two 64-bit perceptual hashes.
   * Distance <= 8 indicates a duplicate or near-duplicate image.
   */
  fun hammingDistance(hash1: Long, hash2: Long): Int {
    return java.lang.Long.bitCount(hash1 xor hash2)
  }

  /**
   * Generates bilingual titles (Arabic & English), tags, and random ~30% isPremium flag
   * for an approved wallpaper candidate.
   */
  fun buildApprovalMetadata(
    category: BulkGenCategory,
    variation: PromptVariation,
    indexSeed: Int
  ): Triple<String, String, List<String>> {
    val titleAr = "${variation.subjectAr} - ${variation.colorAr}"
    val cleanSubjectEn = variation.subjectVar
      .replaceFirstChar { it.uppercase() }
      .split(" ")
      .take(5)
      .joinToString(" ")
    val cleanColorEn = variation.colorVar.split(" ").take(2).joinToString(" ")
      .replaceFirstChar { it.uppercase() }
    val titleEn = "$cleanSubjectEn ($cleanColorEn)"

    val tags = mutableSetOf<String>()
    tags.add(category.titleEn.replace(" ", ""))
    tags.add(category.titleAr)
    variation.colorVar.split(" ").firstOrNull()?.let { tags.add(it.replaceFirstChar { c -> c.uppercase() }) }
    variation.styleVar.split(" ").firstOrNull()?.let { tags.add(it.replaceFirstChar { c -> c.uppercase() }) }
    variation.patternVar.split(" ").take(2).forEach {
      if (it.length > 3) tags.add(it.replaceFirstChar { c -> c.uppercase() })
    }
    tags.add("4K")
    tags.add("IslamicArt")

    return Triple(titleAr, titleEn, tags.take(6))
  }

  /**
   * Randomly marks ~30% of approved wallpapers as Premium.
   */
  fun shouldMarkPremium(): Boolean {
    return Random.nextFloat() < 0.30f
  }

  /**
   * Serializes a list of Wallpapers into the required JSON schema:
   * (id, title_ar, title_en, category, colors, tags, isPremium, imageUrl)
   */
  fun serializeWallpapersToJson(wallpapers: List<Wallpaper>): String {
    val jsonArray = JSONArray()
    for (w in wallpapers) {
      val obj = JSONObject().apply {
        put("id", w.id)
        put("title_ar", w.titleAr)
        put("title_en", w.title)
        put("title", w.title)
        put("titleAr", w.titleAr)
        put("category", w.category.name)
        put("colors", JSONArray(w.colors))
        put("tags", JSONArray(w.tags))
        put("isPremium", w.isPremium)
        put("imageUrl", w.imageUrl)
        put("artist", w.artist)
        put("downloads", w.downloads)
        put("likes", w.likes)
      }
      jsonArray.put(obj)
    }
    return jsonArray.toString(2)
  }

  /**
   * Saves the updated wallpapers JSON to internal storage (`filesDir/wallpapers_generated.json`)
   * so approved items persist across sessions.
   */
  fun saveWallpapersJsonToInternalStorage(context: Context, wallpapers: List<Wallpaper>): File {
    val file = File(context.filesDir, "wallpapers.json")
    file.writeText(serializeWallpapersToJson(wallpapers))
    return file
  }

  /**
   * Exports all approved candidate images and `wallpapers.json` into a single `.zip` archive
   * in the device's Downloads folder via MediaStore.
   */
  fun exportApprovedAsZip(
    context: Context,
    approvedCandidates: List<GeneratedWallpaperCandidate>,
    allWallpapers: List<Wallpaper>
  ): String {
    val zipFileName = "ArabicWalls_BulkExport_${System.currentTimeMillis()}.zip"
    val tempZipFile = File(context.cacheDir, zipFileName)

    ZipOutputStream(BufferedOutputStream(FileOutputStream(tempZipFile))).use { zos ->
      // 1. Write wallpapers.json entry
      val approvedWallpapersOnly = allWallpapers.filter { w ->
        approvedCandidates.any { it.id == w.id }
      }.ifEmpty { allWallpapers }

      val jsonBytes = serializeWallpapersToJson(approvedWallpapersOnly).toByteArray(Charsets.UTF_8)
      val jsonEntry = ZipEntry("wallpapers.json")
      zos.putNextEntry(jsonEntry)
      zos.write(jsonBytes)
      zos.closeEntry()

      // 2. Write each approved image file
      for (candidate in approvedCandidates) {
        val path = candidate.bitmapPath ?: continue
        val imgFile = File(path)
        if (imgFile.exists()) {
          val entryName = "images/${candidate.id}.png"
          zos.putNextEntry(ZipEntry(entryName))
          FileInputStream(imgFile).use { fis ->
            fis.copyTo(zos)
          }
          zos.closeEntry()
        }
      }
    }

    // Copy tempZipFile to public Downloads via MediaStore
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, zipFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/ArabicWalls")
      }
      val resolver = context.contentResolver
      val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
      if (uri != null) {
        resolver.openOutputStream(uri)?.use { out ->
          FileInputStream(tempZipFile).use { input ->
            input.copyTo(out)
          }
        }
      }
    }

    return "Downloads/ArabicWalls/$zipFileName"
  }
}

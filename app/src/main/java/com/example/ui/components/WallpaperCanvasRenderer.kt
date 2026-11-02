package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.model.Wallpaper
import com.example.model.WallpaperPatternType
import java.io.File
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun WallpaperCanvasVisual(
  wallpaper: Wallpaper,
  modifier: Modifier = Modifier,
  dimOverlay: Float = 0f
) {
  val context = LocalContext.current
  val amiriTypeface = ResourcesCompat.getFont(context, R.font.amiri)
  val cairoTypeface = ResourcesCompat.getFont(context, R.font.cairo)

  // Check if wallpaper has a locally saved AI-generated image file
  val localBitmap = remember(wallpaper.imageUrl) {
    val path = wallpaper.imageUrl.removePrefix("file://")
    if (path.isNotBlank() && path.startsWith("/") && File(path).exists()) {
      BitmapFactory.decodeFile(path)
    } else {
      null
    }
  }

  Box(modifier = modifier) {
    if (localBitmap != null) {
      Image(
        bitmap = localBitmap.asImageBitmap(),
        contentDescription = wallpaper.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      if (dimOverlay > 0f) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = dimOverlay.coerceIn(0f, 0.95f)))
        )
      }
    } else {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

      // 1. Background gradient
      val gradientBrush = Brush.verticalGradient(
        colors = listOf(
          Color(wallpaper.primaryGradientStart),
          Color(wallpaper.primaryGradientEnd)
        ),
        startY = 0f,
        endY = h
      )
      drawRect(brush = gradientBrush, size = size)

      // 2. Radial gold/accent ambiance in center
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(wallpaper.accentColor).copy(alpha = 0.18f),
            Color.Transparent
          ),
          center = Offset(w / 2f, h * 0.45f),
          radius = min(w, h) * 0.65f
        ),
        center = Offset(w / 2f, h * 0.45f),
        radius = min(w, h) * 0.65f
      )

      // 3. Draw pattern according to patternType
      when (wallpaper.patternType) {
        WallpaperPatternType.GEOMETRIC_STAR -> {
          drawIslamicStarPattern(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.GOLD_ARABIC_LETTER -> {
          drawArabicLetterArt(w, h, wallpaper.glyphOrLetter, Color(wallpaper.accentColor), amiriTypeface)
        }
        WallpaperPatternType.CALLIGRAPHY_GLYPH -> {
          drawCalligraphyMasterpiece(w, h, wallpaper.glyphOrLetter, Color(wallpaper.accentColor), amiriTypeface)
        }
        WallpaperPatternType.CRESCENT_DUNES -> {
          drawCrescentAndDunes(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.ISLAMIC_ARCH -> {
          drawIslamicArch(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.RAMADAN_LANTERN -> {
          drawRamadanFanous(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.ARABESQUE_ROSETTE -> {
          drawArabesqueRosette(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.MINIMAL_DARK_MOSAIC -> {
          drawKuficDarkGrid(w, h, Color(wallpaper.accentColor))
        }
        WallpaperPatternType.NATURE_OASIS -> {
          drawEmeraldOasis(w, h, Color(wallpaper.accentColor))
        }
      }

      // 4. Subtle gold noise dust / stars overlay
      drawStardust(w, h, Color(wallpaper.accentColor))

      // 5. Dim overlay if requested (used in Quote editor)
      if (dimOverlay > 0f) {
        drawRect(
          color = Color.Black.copy(alpha = dimOverlay.coerceIn(0f, 0.95f)),
          size = size
        )
      }
      }
    }
  }
}

// Procedural Art Generators for Canvas
private fun DrawScope.drawIslamicStarPattern(w: Float, h: Float, accent: Color) {
  val cx = w / 2f
  val cy = h * 0.42f
  val baseRadius = min(w, h) * 0.35f

  // Outer decorative circle
  drawCircle(
    color = accent.copy(alpha = 0.3f),
    center = Offset(cx, cy),
    radius = baseRadius * 1.15f,
    style = Stroke(width = 2f)
  )

  // Double 8-pointed star
  for (radiusMult in listOf(1.0f, 0.72f, 0.48f)) {
    val r = baseRadius * radiusMult
    val strokeWidth = if (radiusMult == 1.0f) 3f else 1.5f

    // Star square 1
    val path1 = androidx.compose.ui.graphics.Path().apply {
      moveTo(cx + r, cy)
      lineTo(cx, cy + r)
      lineTo(cx - r, cy)
      lineTo(cx, cy - r)
      close()
    }
    drawPath(path1, color = accent.copy(alpha = 0.5f), style = Stroke(strokeWidth))

    // Star square 2 rotated 45 deg
    val diag = r * 0.7071f
    val path2 = androidx.compose.ui.graphics.Path().apply {
      moveTo(cx + diag, cy + diag)
      lineTo(cx - diag, cy + diag)
      lineTo(cx - diag, cy - diag)
      lineTo(cx + diag, cy - diag)
      close()
    }
    drawPath(path2, color = accent.copy(alpha = 0.5f), style = Stroke(strokeWidth))
  }

  // Radiating rays
  for (i in 0 until 16) {
    val angle = (i * 22.5 * PI / 180).toFloat()
    val startX = cx + (baseRadius * 0.48f) * cos(angle)
    val startY = cy + (baseRadius * 0.48f) * sin(angle)
    val endX = cx + (baseRadius * 1.15f) * cos(angle)
    val endY = cy + (baseRadius * 1.15f) * sin(angle)
    drawLine(
      color = accent.copy(alpha = 0.22f),
      start = Offset(startX, startY),
      end = Offset(endX, endY),
      strokeWidth = 1f
    )
  }
}

private fun DrawScope.drawArabesqueRosette(w: Float, h: Float, accent: Color) {
  val cx = w / 2f
  val cy = h * 0.44f
  val maxR = min(w, h) * 0.38f

  // Multiple concentric petal rings
  for (ring in 1..4) {
    val r = maxR * (ring / 4f)
    val petals = 12
    for (p in 0 until petals) {
      val angle = (p * (360f / petals) * PI / 180f).toFloat()
      val px = cx + r * cos(angle)
      val py = cy + r * sin(angle)
      drawCircle(
        color = accent.copy(alpha = 0.18f + (ring * 0.05f)),
        center = Offset(px, py),
        radius = r * 0.32f,
        style = Stroke(1.5f)
      )
    }
  }

  drawCircle(
    color = accent,
    center = Offset(cx, cy),
    radius = maxR * 0.15f,
    style = Stroke(3f)
  )
}

private fun DrawScope.drawArabicLetterArt(
  w: Float,
  h: Float,
  letter: String,
  accent: Color,
  typeface: Typeface?
) {
  val cx = w / 2f
  val cy = h * 0.42f
  val radius = min(w, h) * 0.34f

  // Ornamental enclosing frame
  drawCircle(
    color = accent.copy(alpha = 0.35f),
    center = Offset(cx, cy),
    radius = radius,
    style = Stroke(2.5f)
  )
  drawCircle(
    color = accent.copy(alpha = 0.15f),
    center = Offset(cx, cy),
    radius = radius * 0.85f,
    style = Stroke(1f)
  )

  // Draw Letter via native Canvas
  drawContext.canvas.nativeCanvas.apply {
    val paint = Paint().apply {
      isAntiAlias = true
      color = accent.hashCode()
      textAlign = Paint.Align.CENTER
      textSize = min(w, h) * 0.45f
      this.typeface = typeface ?: Typeface.SERIF
      setShadowLayer(30f, 0f, 0f, accent.copy(alpha = 0.7f).hashCode())
    }
    drawText(letter, cx, cy + (min(w, h) * 0.16f), paint)
  }
}

private fun DrawScope.drawCalligraphyMasterpiece(
  w: Float,
  h: Float,
  text: String,
  accent: Color,
  typeface: Typeface?
) {
  val cx = w / 2f
  val cy = h * 0.42f
  val maxR = min(w, h) * 0.36f

  // Glowing halo
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(accent.copy(alpha = 0.3f), Color.Transparent),
      center = Offset(cx, cy),
      radius = maxR * 1.2f
    ),
    center = Offset(cx, cy),
    radius = maxR * 1.2f
  )

  // Arched borders top and bottom
  drawArc(
    color = accent.copy(alpha = 0.5f),
    startAngle = 180f,
    sweepAngle = 180f,
    useCenter = false,
    topLeft = Offset(cx - maxR, cy - maxR * 0.7f),
    size = Size(maxR * 2, maxR * 1.4f),
    style = Stroke(2f)
  )
  drawArc(
    color = accent.copy(alpha = 0.5f),
    startAngle = 0f,
    sweepAngle = 180f,
    useCenter = false,
    topLeft = Offset(cx - maxR, cy - maxR * 0.7f),
    size = Size(maxR * 2, maxR * 1.4f),
    style = Stroke(2f)
  )

  // Text
  drawContext.canvas.nativeCanvas.apply {
    val paint = Paint().apply {
      isAntiAlias = true
      color = accent.hashCode()
      textAlign = Paint.Align.CENTER
      textSize = if (text.length > 5) min(w, h) * 0.18f else min(w, h) * 0.30f
      this.typeface = typeface ?: Typeface.SERIF
      setShadowLayer(35f, 0f, 0f, accent.hashCode())
    }
    drawText(text, cx, cy + (min(w, h) * 0.06f), paint)
  }
}

private fun DrawScope.drawCrescentAndDunes(w: Float, h: Float, accent: Color) {
  val cx = w * 0.5f
  val moonCy = h * 0.28f
  val moonR = min(w, h) * 0.22f

  // Luminous crescent moon
  val moonPath = androidx.compose.ui.graphics.Path().apply {
    // Outer circle
    addOval(androidx.compose.ui.geometry.Rect(cx - moonR, moonCy - moonR, cx + moonR, moonCy + moonR))
  }
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(accent.copy(alpha = 0.25f), Color.Transparent),
      center = Offset(cx, moonCy),
      radius = moonR * 1.8f
    ),
    center = Offset(cx, moonCy),
    radius = moonR * 1.8f
  )
  // Draw crescent via native arc/subtraction or overlapping
  drawCircle(
    color = accent,
    center = Offset(cx, moonCy),
    radius = moonR
  )
  // Dark cutting sphere to make crescent
  drawCircle(
    color = Color(0xFF090D14),
    center = Offset(cx + moonR * 0.42f, moonCy - moonR * 0.15f),
    radius = moonR * 0.92f
  )

  // Sand Dunes at bottom
  val duneY1 = h * 0.68f
  val dunePath1 = androidx.compose.ui.graphics.Path().apply {
    moveTo(0f, duneY1)
    quadraticTo(w * 0.4f, duneY1 - 60f, w * 0.8f, duneY1 + 40f)
    lineTo(w, duneY1 + 20f)
    lineTo(w, h)
    lineTo(0f, h)
    close()
  }
  drawPath(
    path = dunePath1,
    brush = Brush.verticalGradient(
      colors = listOf(accent.copy(alpha = 0.35f), Color.Black),
      startY = duneY1 - 60f,
      endY = h
    )
  )

  val duneY2 = h * 0.78f
  val dunePath2 = androidx.compose.ui.graphics.Path().apply {
    moveTo(0f, duneY2 + 30f)
    quadraticTo(w * 0.6f, duneY2 - 40f, w, duneY2)
    lineTo(w, h)
    lineTo(0f, h)
    close()
  }
  drawPath(
    path = dunePath2,
    brush = Brush.verticalGradient(
      colors = listOf(accent.copy(alpha = 0.5f), Color.Black),
      startY = duneY2 - 40f,
      endY = h
    )
  )
}

private fun DrawScope.drawIslamicArch(w: Float, h: Float, accent: Color) {
  val cx = w / 2f
  val archTop = h * 0.18f
  val archBottom = h * 0.82f
  val archW = w * 0.75f
  val left = cx - archW / 2f
  val right = cx + archW / 2f

  for (offset in listOf(0f, 16f, 32f)) {
    val oLeft = left + offset
    val oRight = right - offset
    val oTop = archTop + offset
    val oWidth = oRight - oLeft

    val path = androidx.compose.ui.graphics.Path().apply {
      moveTo(oLeft, archBottom)
      lineTo(oLeft, oTop + oWidth * 0.5f)
      cubicTo(
        oLeft, oTop + oWidth * 0.1f,
        cx - oWidth * 0.2f, oTop,
        cx, oTop
      )
      cubicTo(
        cx + oWidth * 0.2f, oTop,
        oRight, oTop + oWidth * 0.1f,
        oRight, oTop + oWidth * 0.5f
      )
      lineTo(oRight, archBottom)
    }

    drawPath(
      path = path,
      color = accent.copy(alpha = 0.6f - (offset * 0.01f)),
      style = Stroke(if (offset == 0f) 3f else 1.5f)
    )
  }

  // Mihrab Lamp hanging in center of arch
  drawLine(
    color = accent.copy(alpha = 0.6f),
    start = Offset(cx, archTop + 32f),
    end = Offset(cx, archTop + 140f),
    strokeWidth = 2f
  )
  drawCircle(
    color = accent,
    center = Offset(cx, archTop + 160f),
    radius = 18f
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(accent.copy(alpha = 0.4f), Color.Transparent),
      center = Offset(cx, archTop + 160f),
      radius = 50f
    ),
    center = Offset(cx, archTop + 160f),
    radius = 50f
  )
}

private fun DrawScope.drawRamadanFanous(w: Float, h: Float, accent: Color) {
  val cx = w / 2f
  val topY = h * 0.16f
  val lanternH = min(w, h) * 0.5f

  // Hanging chain
  drawLine(
    color = accent.copy(alpha = 0.7f),
    start = Offset(cx, 0f),
    end = Offset(cx, topY),
    strokeWidth = 2.5f
  )

  // Lantern Top Dome & Crescent
  drawCircle(
    color = accent,
    center = Offset(cx, topY),
    radius = 14f
  )

  // Lantern Body
  val path = androidx.compose.ui.graphics.Path().apply {
    moveTo(cx - 30f, topY + 20f)
    lineTo(cx + 30f, topY + 20f)
    lineTo(cx + 65f, topY + lanternH * 0.45f)
    lineTo(cx + 40f, topY + lanternH * 0.85f)
    lineTo(cx - 40f, topY + lanternH * 0.85f)
    lineTo(cx - 65f, topY + lanternH * 0.45f)
    close()
  }

  // Lantern Glow inside
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(accent.copy(alpha = 0.5f), Color.Transparent),
      center = Offset(cx, topY + lanternH * 0.45f),
      radius = lanternH * 0.6f
    ),
    center = Offset(cx, topY + lanternH * 0.45f),
    radius = lanternH * 0.6f
  )

  drawPath(
    path = path,
    color = accent.copy(alpha = 0.8f),
    style = Stroke(3.5f)
  )

  // Decorative internal glass ribs
  drawLine(
    color = accent.copy(alpha = 0.5f),
    start = Offset(cx, topY + 20f),
    end = Offset(cx, topY + lanternH * 0.85f),
    strokeWidth = 2f
  )
}

private fun DrawScope.drawKuficDarkGrid(w: Float, h: Float, accent: Color) {
  val step = 48f
  var y = 0f
  while (y < h) {
    drawLine(
      color = accent.copy(alpha = 0.08f),
      start = Offset(0f, y),
      end = Offset(w, y),
      strokeWidth = 1f
    )
    y += step
  }
  var x = 0f
  while (x < w) {
    drawLine(
      color = accent.copy(alpha = 0.08f),
      start = Offset(x, 0f),
      end = Offset(x, h),
      strokeWidth = 1f
    )
    x += step
  }

  // Interspersed gold diamond accents
  for (i in 2..8) {
    for (j in 3..14) {
      if ((i + j) % 4 == 0) {
        val px = i * step
        val py = j * step
        drawCircle(
          color = accent.copy(alpha = 0.4f),
          center = Offset(px, py),
          radius = 3.5f
        )
      }
    }
  }
}

private fun DrawScope.drawEmeraldOasis(w: Float, h: Float, accent: Color) {
  val cx = w / 2f
  val cy = h * 0.45f

  // Moon
  drawCircle(
    color = accent.copy(alpha = 0.85f),
    center = Offset(cx, h * 0.22f),
    radius = 38f
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(accent.copy(alpha = 0.3f), Color.Transparent),
      center = Offset(cx, h * 0.22f),
      radius = 120f
    ),
    center = Offset(cx, h * 0.22f),
    radius = 120f
  )

  // Palm leaves curves
  val palmBase = Offset(cx, h * 0.75f)
  for (i in -3..3) {
    val leafEnd = Offset(cx + (i * 90f), h * 0.48f + (kotlin.math.abs(i) * 35f))
    drawLine(
      color = accent.copy(alpha = 0.7f),
      start = palmBase,
      end = leafEnd,
      strokeWidth = 3f
    )
  }
}

private fun DrawScope.drawStardust(w: Float, h: Float, accent: Color) {
  // Deterministic star field
  val points = listOf(
    Offset(w * 0.15f, h * 0.12f),
    Offset(w * 0.82f, h * 0.18f),
    Offset(w * 0.28f, h * 0.32f),
    Offset(w * 0.74f, h * 0.38f),
    Offset(w * 0.12f, h * 0.65f),
    Offset(w * 0.88f, h * 0.72f),
    Offset(w * 0.50f, h * 0.85f),
    Offset(w * 0.35f, h * 0.08f),
    Offset(w * 0.68f, h * 0.15f)
  )

  for (pt in points) {
    drawCircle(
      color = accent.copy(alpha = 0.6f),
      center = pt,
      radius = 2.2f
    )
  }
}

/**
 * Utility to generate a high-res Bitmap representation for WallpaperManager or saving
 */
fun createWallpaperBitmap(
  context: android.content.Context,
  wallpaper: Wallpaper,
  quoteText: String? = null,
  quoteSource: String? = null,
  fontCairo: Boolean = true,
  fontSizeSp: Float = 24f,
  fontColorHex: Int = 0xFFFFFFFF.toInt(),
  dimAlpha: Float = 0f,
  width: Int = 1080,
  height: Int = 1920
): Bitmap {
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  val cx = width / 2f
  val cy = height * 0.42f

  val typeface = if (fontCairo) {
    ResourcesCompat.getFont(context, R.font.cairo)
  } else {
    ResourcesCompat.getFont(context, R.font.amiri)
  }

  val localPath = wallpaper.imageUrl.removePrefix("file://")
  val localFile = if (localPath.isNotBlank() && localPath.startsWith("/")) File(localPath) else null
  val decodedLocal = if (localFile != null && localFile.exists()) BitmapFactory.decodeFile(localFile.absolutePath) else null

  if (decodedLocal != null) {
    val destRect = android.graphics.Rect(0, 0, width, height)
    canvas.drawBitmap(decodedLocal, null, destRect, Paint(Paint.FILTER_BITMAP_FLAG))
  } else {
    // Background
    val bgPaint = Paint().apply {
      isAntiAlias = true
      val gradient = android.graphics.LinearGradient(
        0f, 0f, 0f, height.toFloat(),
        wallpaper.primaryGradientStart.toInt(),
        wallpaper.primaryGradientEnd.toInt(),
        android.graphics.Shader.TileMode.CLAMP
      )
      shader = gradient
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Radial accent
    val radialPaint = Paint().apply {
      isAntiAlias = true
      val rad = android.graphics.RadialGradient(
        width / 2f, height * 0.45f,
        width * 0.65f,
        Color(wallpaper.accentColor).copy(alpha = 0.25f).hashCode(),
        0x00000000,
        android.graphics.Shader.TileMode.CLAMP
      )
      shader = rad
    }
    canvas.drawCircle(width / 2f, height * 0.45f, width * 0.65f, radialPaint)

    // Procedural motif
    val accentPaint = Paint().apply {
      isAntiAlias = true
      color = wallpaper.accentColor.toInt()
      style = Paint.Style.STROKE
      strokeWidth = 4f
    }

    // Draw big decorative circles & star
    canvas.drawCircle(cx, cy, width * 0.35f, accentPaint)

    val glyphPaint = Paint().apply {
      isAntiAlias = true
      color = wallpaper.accentColor.toInt()
      textAlign = Paint.Align.CENTER
      textSize = width * 0.22f
      this.typeface = typeface ?: Typeface.SERIF
      setShadowLayer(40f, 0f, 0f, wallpaper.accentColor.toInt())
    }
    canvas.drawText(wallpaper.glyphOrLetter, cx, cy + (width * 0.08f), glyphPaint)
  }

  // Dim overlay if quote is present
  if (dimAlpha > 0f) {
    val dimPaint = Paint().apply {
      isAntiAlias = true
      color = Color.Black.copy(alpha = dimAlpha).hashCode()
      style = Paint.Style.FILL
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
  }

  // Quote text if provided
  if (!quoteText.isNullOrBlank()) {
    val qPaint = Paint().apply {
      isAntiAlias = true
      color = fontColorHex
      textAlign = Paint.Align.CENTER
      textSize = fontSizeSp * (width / 400f)
      this.typeface = typeface ?: Typeface.SERIF
      setShadowLayer(20f, 0f, 0f, 0x88000000.toInt())
    }

    // Split text into lines if long
    val words = quoteText.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""
    for (w in words) {
      val testLine = if (currentLine.isEmpty()) w else "$currentLine $w"
      if (qPaint.measureText(testLine) < width * 0.85f) {
        currentLine = testLine
      } else {
        lines.add(currentLine)
        currentLine = w
      }
    }
    if (currentLine.isNotEmpty()) lines.add(currentLine)

    val startY = height * 0.52f - (lines.size * qPaint.textSize * 0.6f)
    lines.forEachIndexed { index, line ->
      canvas.drawText(line, cx, startY + (index * qPaint.textSize * 1.35f), qPaint)
    }

    if (!quoteSource.isNullOrBlank()) {
      val srcPaint = Paint().apply {
        isAntiAlias = true
        color = Color(fontColorHex).copy(alpha = 0.75f).hashCode()
        textAlign = Paint.Align.CENTER
        textSize = (fontSizeSp * 0.65f) * (width / 400f)
        this.typeface = typeface ?: Typeface.SERIF
      }
      canvas.drawText(quoteSource, cx, startY + (lines.size * qPaint.textSize * 1.35f) + 40f, srcPaint)
    }
  }

  return bitmap
}

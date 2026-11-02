package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PromptTemplateEngine
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.BulkGenCategory
import com.example.model.CandidateStatus
import com.example.model.GeneratedWallpaperCandidate
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkGoldBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PremiumBadgeText
import com.example.viewmodel.BulkGeneratorState
import java.io.File

@Composable
fun AdminBulkGeneratorScreen(
  state: BulkGeneratorState,
  language: AppLanguage,
  onSelectCategory: (BulkGenCategory) -> Unit,
  onSelectQuantity: (Int) -> Unit,
  onUpdateDailyLimit: (Int) -> Unit,
  onResetDailyCounter: () -> Unit,
  onStartGeneration: () -> Unit,
  onStopGeneration: () -> Unit,
  onApproveCandidate: (String) -> Unit,
  onRejectCandidate: (String) -> Unit,
  onRegenerateCandidate: (String) -> Unit,
  onApproveAllGenerated: () -> Unit,
  onRemoveDuplicates: () -> Unit,
  onExportZip: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sampleVariation = remember(state.selectedCategory) {
    PromptTemplateEngine.generateUniquePrompts(state.selectedCategory, 1).firstOrNull()
  }

  val visibleCandidates = remember(state.candidates) {
    state.candidates.filter { it.status != CandidateStatus.DUPLICATE_REMOVED }
  }

  val approvedCount = remember(state.candidates) {
    state.candidates.count { it.status == CandidateStatus.APPROVED }
  }

  val readyToApproveCount = remember(state.candidates) {
    state.candidates.count { it.status == CandidateStatus.GENERATED }
  }

  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 96.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
      .testTag("admin_bulk_generator_grid")
  ) {
    // 1. Header & Backend Proxy Security Banner
    item(span = { GridItemSpan(2) }) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Brush.radialGradient(listOf(GoldLight, GoldPrimary))),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = DarkBackground,
              modifier = Modifier.size(22.dp)
            )
          }
          Column {
            Text(
              text = Strings.get("admin_title"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color.White
            )
            Text(
              text = Strings.get("admin_subtitle"),
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = GoldMetallic
            )
          }
        }

        // Backend Proxy & Daily Limit Card
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFF10B981),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = Strings.get("backend_proxy_status"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              color = Color(0xFF34D399)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${Strings.get("daily_limit_title")}: ${state.dailyUsedCount} / ${state.dailyLimit}",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color.White
            )
            TextButton(
              onClick = onResetDailyCounter,
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              modifier = Modifier.height(28.dp).testTag("reset_daily_limit_btn")
            ) {
              Text(
                text = Strings.get("reset_daily_counter"),
                fontFamily = CairoFontFamily,
                fontSize = 11.sp,
                color = GoldLight
              )
            }
          }

          Slider(
            value = state.dailyLimit.toFloat(),
            onValueChange = { onUpdateDailyLimit(it.toInt()) },
            valueRange = 10f..250f,
            steps = 23,
            colors = SliderDefaults.colors(
              thumbColor = GoldPrimary,
              activeTrackColor = GoldPrimary,
              inactiveTrackColor = DarkBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(26.dp)
              .testTag("daily_limit_slider")
          )
        }
      }
    }

    // 2. Category Selector (Arabic Letters, Nature, Occasions, Geometric, Dark, Ramadan, Eid)
    item(span = { GridItemSpan(2) }) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = Strings.get("select_category"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color.White
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          BulkGenCategory.entries.forEach { cat ->
            val selected = state.selectedCategory == cat
            val label = if (language == AppLanguage.ARABIC) cat.titleAr else cat.titleEn
            val bgModifier = if (selected) {
              Modifier.background(Brush.horizontalGradient(listOf(GoldPrimary, GoldLight)))
            } else {
              Modifier.background(DarkSurfaceElevated)
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .then(bgModifier)
                .border(
                  width = 1.dp,
                  color = if (selected) GoldLight else DarkBorder,
                  shape = RoundedCornerShape(18.dp)
                )
                .clickable { onSelectCategory(cat) }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("bulk_cat_${cat.id}"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                fontFamily = CairoFontFamily,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                color = if (selected) DarkBackground else Color.White
              )
            }
          }
        }
      }
    }

    // 3. Quantity Selector (10, 25, 50, 100) + Prompt Combinator Preview
    item(span = { GridItemSpan(2) }) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = Strings.get("select_quantity"),
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color.White
        )

        val quantities = listOf(10, 25, 50, 100)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          quantities.forEach { qty ->
            val selected = state.selectedQuantity == qty
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(if (selected) GoldPrimary else DarkSurfaceElevated)
                .border(
                  width = 1.dp,
                  color = if (selected) GoldLight else DarkBorder,
                  shape = RoundedCornerShape(14.dp)
                )
                .clickable { onSelectQuantity(qty) }
                .padding(vertical = 10.dp)
                .testTag("bulk_qty_$qty"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "$qty",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (selected) DarkBackground else Color.White
              )
            }
          }
        }

        // Dynamic Prompt Template Engine Preview Card
        if (sampleVariation != null) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(DarkSurfaceVariant)
              .border(1.dp, DarkGoldBorder, RoundedCornerShape(14.dp))
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = Strings.get("prompt_engine_preview"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = GoldLight
            )
            Text(
              text = sampleVariation.fullPrompt,
              fontFamily = CairoFontFamily,
              fontSize = 10.sp,
              lineHeight = 14.sp,
              color = Color(0xFFCBD5E1),
              maxLines = 3,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }

    // 4. Start / Stop Generation Button + Parallel Batch Progress Bar
    item(span = { GridItemSpan(2) }) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (state.isGenerating) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(DarkSurfaceElevated)
              .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
              .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                CircularProgressIndicator(
                  color = GoldPrimary,
                  strokeWidth = 2.5.dp,
                  modifier = Modifier.size(18.dp)
                )
                Text(
                  text = if (language == AppLanguage.ARABIC) {
                    "جاري التوليد المتوازي (4 معاً): ${state.completedInBatch} / ${state.totalInBatch}"
                  } else {
                    "Parallel Batch Generation (4x): ${state.completedInBatch} / ${state.totalInBatch}"
                  },
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = GoldLight
                )
              }

              OutlinedButton(
                onClick = onStopGeneration,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp).testTag("stop_bulk_gen_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.Stop,
                  contentDescription = null,
                  tint = Color(0xFFF43F5E),
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = Strings.get("stop_generation"),
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  color = Color(0xFFF43F5E)
                )
              }
            }

            val progress = if (state.totalInBatch > 0) {
              state.completedInBatch.toFloat() / state.totalInBatch.toFloat()
            } else 0f

            LinearProgressIndicator(
              progress = { progress },
              color = GoldPrimary,
              trackColor = DarkBorder,
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .testTag("bulk_generation_progress_bar")
            )
          }
        } else {
          Button(
            onClick = onStartGeneration,
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("start_bulk_gen_btn")
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = DarkBackground,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "${Strings.get("start_bulk_generation")} (${state.selectedQuantity})",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = DarkBackground
            )
          }
        }

        // Toolbar Row: Approve All Ready, Remove Duplicates, Export ZIP
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Approve All Ready
          OutlinedButton(
            onClick = onApproveAllGenerated,
            enabled = readyToApproveCount > 0,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
            modifier = Modifier.weight(1f).testTag("approve_all_btn")
          ) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = null,
              tint = if (readyToApproveCount > 0) Color(0xFF34D399) else Color.Gray,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${Strings.get("approve_all")} ($readyToApproveCount)",
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = if (readyToApproveCount > 0) Color.White else Color.Gray,
              maxLines = 1
            )
          }

          // Deduplicate
          OutlinedButton(
            onClick = onRemoveDuplicates,
            enabled = state.candidates.isNotEmpty(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
            modifier = Modifier.weight(1f).testTag("deduplicate_btn")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = null,
              tint = GoldLight,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = Strings.get("remove_duplicates"),
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = Color.White,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Export ZIP Button
        Button(
          onClick = onExportZip,
          enabled = !state.isExportingZip,
          colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GoldPrimary.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            .testTag("export_zip_btn")
        ) {
          if (state.isExportingZip) {
            CircularProgressIndicator(
              color = GoldPrimary,
              strokeWidth = 2.dp,
              modifier = Modifier.size(18.dp)
            )
          } else {
            Icon(
              imageVector = Icons.Default.FolderZip,
              contentDescription = null,
              tint = GoldPrimary,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "${Strings.get("export_zip")} ($approvedCount)",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = GoldLight
          )
        }
      }
    }

    // 5. Review Grid Title & Stats
    if (visibleCandidates.isNotEmpty()) {
      item(span = { GridItemSpan(2) }) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${Strings.get("review_grid_title")} (${visibleCandidates.size})",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
          )
          if (state.duplicatesRemovedCount > 0) {
            Text(
              text = if (language == AppLanguage.ARABIC) {
                "تم استبعاد ${state.duplicatesRemovedCount} مكرر"
              } else {
                "${state.duplicatesRemovedCount} duplicate(s) removed"
              },
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = Color(0xFFFBBF24)
            )
          }
        }
      }
    }

    // 6. Review Grid Cards (Approve, Reject, Regenerate)
    items(visibleCandidates, key = { it.id }) { candidate ->
      CandidateReviewCard(
        candidate = candidate,
        language = language,
        onApprove = { onApproveCandidate(candidate.id) },
        onReject = { onRejectCandidate(candidate.id) },
        onRegenerate = { onRegenerateCandidate(candidate.id) }
      )
    }
  }
}

@Composable
private fun CandidateReviewCard(
  candidate: GeneratedWallpaperCandidate,
  language: AppLanguage,
  onApprove: () -> Unit,
  onReject: () -> Unit,
  onRegenerate: () -> Unit
) {
  val bitmap = remember(candidate.bitmapPath, candidate.status) {
    val path = candidate.bitmapPath
    if (path != null && File(path).exists()) {
      BitmapFactory.decodeFile(path)
    } else {
      null
    }
  }

  val borderColor = when (candidate.status) {
    CandidateStatus.APPROVED -> Color(0xFF10B981)
    CandidateStatus.REJECTED -> Color(0xFFE11D48)
    CandidateStatus.ERROR -> Color(0xFFF59E0B)
    else -> DarkBorder
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(DarkSurfaceElevated)
      .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
      .testTag("candidate_card_${candidate.id}")
  ) {
    // 9:19.5 vertical phone wallpaper preview box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(9f / 16f)
        .background(DarkSurface),
      contentAlignment = Alignment.Center
    ) {
      when (candidate.status) {
        CandidateStatus.PENDING -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp)
          ) {
            CircularProgressIndicator(
              color = GoldMetallic.copy(alpha = 0.6f),
              strokeWidth = 2.dp,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = Strings.get("status_pending"),
              fontFamily = CairoFontFamily,
              fontSize = 11.sp,
              color = Color.LightGray,
              textAlign = TextAlign.Center
            )
          }
        }

        CandidateStatus.GENERATING -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp)
          ) {
            CircularProgressIndicator(
              color = GoldPrimary,
              strokeWidth = 3.dp,
              modifier = Modifier.size(32.dp)
            )
            Text(
              text = Strings.get("status_generating"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              color = GoldLight,
              textAlign = TextAlign.Center
            )
            if (candidate.retryCount > 0) {
              Text(
                text = "Retry #${candidate.retryCount}",
                fontFamily = CairoFontFamily,
                fontSize = 10.sp,
                color = Color(0xFFFBBF24)
              )
            }
          }
        }

        CandidateStatus.ERROR -> {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ErrorOutline,
              contentDescription = null,
              tint = Color(0xFFF43F5E),
              modifier = Modifier.size(28.dp)
            )
            Text(
              text = candidate.errorMessage ?: "Generation failed",
              fontFamily = CairoFontFamily,
              fontSize = 10.sp,
              lineHeight = 13.sp,
              color = Color(0xFFFDA4AF),
              textAlign = TextAlign.Center,
              maxLines = 4,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        else -> {
          if (bitmap != null) {
            Image(
              bitmap = bitmap.asImageBitmap(),
              contentDescription = candidate.variation.subjectVar,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }
        }
      }

      // Status & Premium Badges at top
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.TopCenter)
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (candidate.status == CandidateStatus.APPROVED) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xDD059669))
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
              )
              Text(
                text = Strings.get("status_approved"),
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = Color.White
              )
            }
          }
        } else if (candidate.status == CandidateStatus.REJECTED) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xDDE11D48))
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Text(
              text = Strings.get("status_rejected"),
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              color = Color.White
            )
          }
        } else {
          Spacer(modifier = Modifier.width(1.dp))
        }

        if (candidate.status == CandidateStatus.APPROVED && candidate.isPremium) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Brush.horizontalGradient(listOf(GoldPrimary, GoldLight)))
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = PremiumBadgeText,
                modifier = Modifier.size(10.dp)
              )
              Text(
                text = "PRO",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = PremiumBadgeText
              )
            }
          }
        }
      }

      // Extracted Dominant Color Swatches at bottom of preview
      if (candidate.extractedColors.isNotEmpty()) {
        Row(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x99000000))
            .padding(horizontal = 6.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          candidate.extractedColors.forEach { hex ->
            val parsedColor = runCatching { Color(android.graphics.Color.parseColor(hex)) }
              .getOrDefault(GoldPrimary)
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(parsedColor)
                .border(0.5.dp, Color.White, CircleShape)
            )
          }
        }
      }
    }

    // Metadata & Action Controls Footer
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      val displayTitle = if (candidate.status == CandidateStatus.APPROVED) {
        if (language == AppLanguage.ARABIC) candidate.titleAr else candidate.titleEn
      } else {
        if (language == AppLanguage.ARABIC) candidate.variation.subjectAr else candidate.variation.subjectVar
      }

      Text(
        text = displayTitle,
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      // Action Buttons: Approve, Reject, Regenerate
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Approve Button
        IconButton(
          onClick = onApprove,
          enabled = candidate.status == CandidateStatus.GENERATED || candidate.status == CandidateStatus.REJECTED,
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              if (candidate.status == CandidateStatus.GENERATED || candidate.status == CandidateStatus.REJECTED)
                Color(0xFF059669)
              else DarkSurface
            )
            .testTag("approve_btn_${candidate.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = Strings.get("approve"),
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }

        // Reject Button
        IconButton(
          onClick = onReject,
          enabled = candidate.status != CandidateStatus.REJECTED && candidate.status != CandidateStatus.GENERATING,
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurface)
            .border(1.dp, Color(0xFFE11D48).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .testTag("reject_btn_${candidate.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = Strings.get("reject"),
            tint = Color(0xFFF43F5E),
            modifier = Modifier.size(16.dp)
          )
        }

        // Regenerate Button
        IconButton(
          onClick = onRegenerate,
          enabled = candidate.status != CandidateStatus.GENERATING,
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurface)
            .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .testTag("regenerate_btn_${candidate.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = Strings.get("regenerate"),
            tint = GoldLight,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

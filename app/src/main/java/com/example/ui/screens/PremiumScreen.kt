package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WallpaperData
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.PlanDuration
import com.example.model.PremiumPlan
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkGoldBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.OnGoldContainer

@Composable
fun PremiumScreen(
  isPremiumUser: Boolean,
  language: AppLanguage,
  onPurchasePlan: (PremiumPlan) -> Unit,
  onToggleDemoPremium: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  var selectedPlan by remember { mutableStateOf(WallpaperData.premiumPlans[1]) } // default to Yearly

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkBackground)
      .verticalScroll(scrollState)
      .padding(horizontal = 20.dp, vertical = 16.dp)
      .padding(bottom = 80.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Luxury Gold Header Badge & Emblem
    Box(
      modifier = Modifier
        .size(68.dp)
        .clip(CircleShape)
        .background(Brush.radialGradient(listOf(GoldLight, GoldPrimary)))
        .padding(3.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(CircleShape)
          .background(DarkBackground),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(36.dp)
        )
      }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = Strings.get("premium_title"),
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = Color.White,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = Strings.get("premium_subtitle"),
        fontFamily = CairoFontFamily,
        fontSize = 13.sp,
        color = GoldMetallic,
        textAlign = TextAlign.Center
      )
    }

    // 2. Active Status Indicator (if already subscribed)
    if (isPremiumUser) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFF063327))
          .border(1.dp, Color(0xFF10B981), RoundedCornerShape(16.dp))
          .padding(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF34D399),
            modifier = Modifier.size(22.dp)
          )
          Text(
            text = Strings.get("premium_active"),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
          )
        }
      }
    }

    // 3. Plan Selection Cards (Monthly, Yearly, Lifetime)
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      WallpaperData.premiumPlans.forEach { plan ->
        val isSelected = selectedPlan.duration == plan.duration
        val title = if (language == AppLanguage.ARABIC) plan.titleAr else plan.titleEn
        val price = if (language == AppLanguage.ARABIC) plan.priceAr else plan.priceEn
        val tag = if (language == AppLanguage.ARABIC) plan.tagAr else plan.tagEn

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
              if (isSelected) Brush.horizontalGradient(listOf(Color(0xFF261D0C), Color(0xFF191409)))
              else Brush.horizontalGradient(listOf(DarkSurfaceElevated, DarkSurfaceVariant))
            )
            .border(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) GoldPrimary else DarkBorder,
              shape = RoundedCornerShape(18.dp)
            )
            .clickable { selectedPlan = plan }
            .padding(16.dp)
            .testTag("plan_card_${plan.duration.name.lowercase()}")
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .clip(CircleShape)
                  .border(2.dp, if (isSelected) GoldPrimary else Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .size(12.dp)
                      .clip(CircleShape)
                      .background(GoldPrimary)
                  )
                }
              }

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = title,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                  )
                  if (!tag.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GoldPrimary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = tag,
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = DarkBackground
                      )
                    }
                  }
                }
                Text(
                  text = if (plan.duration == PlanDuration.LIFETIME) {
                    if (language == AppLanguage.ARABIC) "دفعة واحدة بلا تجديد تلقائي" else "One-time payment forever"
                  } else {
                    if (language == AppLanguage.ARABIC) "تجديد تلقائي ويمكن الإلغاء بأي وقت" else "Auto-renews, cancel anytime"
                  },
                  fontFamily = CairoFontFamily,
                  fontSize = 11.sp,
                  color = Color(0xFFA0B0C4)
                )
              }
            }

            Text(
              text = price,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = GoldLight
            )
          }
        }
      }
    }

    // 4. Premium Features Checklist
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
        .background(DarkSurface)
        .border(1.dp, DarkBorder, RoundedCornerShape(18.dp))
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      val perks = listOf(
        Strings.get("premium_feature_1"),
        Strings.get("premium_feature_2"),
        Strings.get("premium_feature_3"),
        Strings.get("premium_feature_4")
      )

      perks.forEach { perk ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = perk,
            fontFamily = CairoFontFamily,
            fontSize = 12.sp,
            color = Color(0xFFE2E8F0)
          )
        }
      }
    }

    // 5. Upgrade Action Button
    Button(
      onClick = { onPurchasePlan(selectedPlan) },
      colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("upgrade_plan_btn")
    ) {
      Text(
        text = Strings.get("unlock_now"),
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = DarkBackground
      )
    }

    // 6. Demo Premium Toggle for easy testing of pro wallpapers
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceVariant)
        .padding(horizontal = 14.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = Strings.get("demo_toggle"),
        fontFamily = CairoFontFamily,
        fontSize = 12.sp,
        color = GoldLight
      )
      Switch(
        checked = isPremiumUser,
        onCheckedChange = { onToggleDemoPremium() },
        colors = SwitchDefaults.colors(
          checkedThumbColor = GoldPrimary,
          checkedTrackColor = GoldContainer,
          uncheckedThumbColor = Color.Gray,
          uncheckedTrackColor = DarkBorder
        ),
        modifier = Modifier.testTag("demo_premium_switch")
      )
    }
  }
}

package com.example.projetmobile.screen

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetmobile.DatabaseHelper
import com.example.projetmobile.R

@Composable
fun HighscoreScreen(
    context: Context,
    newScore: Int,
    onMenuClick: () -> Unit
) {
    val colors = AppColors
    val dbHelper = remember { DatabaseHelper(context) }
    val scores = remember { dbHelper.getTop10Scores() }

    val isTop3 = remember(newScore) {
        if (newScore <= 0 || scores.isEmpty()) false
        else {
            val top3 = scores.take(3)
            top3.any { it.score == newScore }
        }
    }

    val podiumColors = listOf(colors.Gold, colors.Silver, colors.Bronze)
    val podiumBg = listOf(
        colors.Gold.copy(alpha = 0.15f),
        colors.Silver.copy(alpha = 0.15f),
        colors.Bronze.copy(alpha = 0.15f)
    )
    val medalEmojis = listOf("🥇", "🥈", "🥉")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.Background)
    ) {
        // Top accent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(colors.PrimaryLight, Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            Text(text = "🏆", fontSize = 56.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.highscores_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            if (scores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎮", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.no_scores),
                            color = colors.TextSecondary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(scores) { index, entry ->
                        val isPodium = index < 3
                        val isNewScoreRow = isPodium && entry.score == newScore

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (isPodium) Modifier.shadow(6.dp, RoundedCornerShape(20.dp))
                                    else Modifier
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isPodium) podiumBg[index] else colors.SurfaceCard
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Rank indicator
                                if (isPodium) {
                                    Text(
                                        text = medalEmojis[index],
                                        fontSize = 32.sp
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(colors.PrimaryLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = colors.Primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Spacer(Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entry.name,
                                        color = if (isPodium) podiumColors[index] else colors.TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = entry.date,
                                        color = colors.TextHint,
                                        fontSize = 12.sp
                                    )
                                }

                                // Score badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(
                                            if (isPodium)
                                                podiumColors[index].copy(alpha = 0.25f)
                                            else colors.PrimaryLight
                                        )
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${entry.score}",
                                        color = if (isPodium) podiumColors[index] else colors.Primary,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onMenuClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.Primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "← ${stringResource(R.string.btn_menu)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        // Overlay Confettis
        if (isTop3) {
            ConfettiHost()
        }
    }
}

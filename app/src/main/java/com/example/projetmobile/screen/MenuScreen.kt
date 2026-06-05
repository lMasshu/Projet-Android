package com.example.projetmobile.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetmobile.R

// Light theme color palette
object AppColors {
    val Background      = Color(0xFFF5F7FF)
    val SurfaceCard     = Color(0xFFFFFFFF)
    val Primary         = Color(0xFF5B5FEF)       // Indigo vif
    val PrimaryLight    = Color(0xFFEEEFFF)
    val Secondary       = Color(0xFF10B981)       // Émeraude
    val SecondaryLight  = Color(0xFFD1FAE5)
    val Error           = Color(0xFFEF4444)
    val ErrorLight      = Color(0xFFFFE4E4)
    val TextPrimary     = Color(0xFF1E1B4B)
    val TextSecondary   = Color(0xFF6B7280)
    val TextHint        = Color(0xFF9CA3AF)
    val Divider         = Color(0xFFE5E7EB)
    val Gold            = Color(0xFFF59E0B)
    val Silver          = Color(0xFF94A3B8)
    val Bronze          = Color(0xFFCD7F32)
}

@Composable
fun MenuScreen(
    onPlayClick: () -> Unit,
    onHighscoreClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Subtle light gradient background
    val gradientBackground = Brush.linearGradient(
        colors = listOf(Color(0xFFF0F4FF), Color(0xFFFAF5FF), Color(0xFFEEF2FF)),
        start = Offset(0f, 0f),
        end = Offset(800f, 1200f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        // Top decorative blob
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0E7FF), Color(0x00E0E7FF))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo emoji
            Text(
                text = "🧮",
                fontSize = 84.sp,
                modifier = Modifier.scale(pulseScale)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App title
            Text(
                text = stringResource(R.string.app_title),
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.app_subtitle),
                fontSize = 16.sp,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Play button – filled, Primary color
            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_play),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Highscores button – outlined
            OutlinedButton(
                onClick = onHighscoreClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, AppColors.Primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppColors.Primary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_highscores),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
            }
        }
    }
}

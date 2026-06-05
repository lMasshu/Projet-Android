package com.example.projetmobile.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetmobile.LanguageManager
import com.example.projetmobile.R

@Composable
fun MenuScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onPlayClick: () -> Unit,
    onHighscoreClick: () -> Unit
) {
    val colors = AppColors

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.07f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val gradientBackground = Brush.linearGradient(
        colors = if (isDarkMode)
            listOf(Color(0xFF0F0E1A), Color(0xFF1A1033), Color(0xFF0D0C1D))
        else
            listOf(Color(0xFFF0F4FF), Color(0xFFFAF5FF), Color(0xFFEEF2FF)),
        start = Offset(0f, 0f),
        end   = Offset(800f, 1200f)
    )

    // Langue courante
    val currentLang = remember { mutableStateOf(LanguageManager.getCurrentLanguage()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        // Blob décoratif
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.PrimaryLight.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        // ── Top bar : Dark mode + Langue ──────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sélecteur de langue FR | EN
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("fr" to "🇫🇷", "en" to "🇬🇧").forEach { (code, flag) ->
                    val isSelected = currentLang.value == code
                    if (isSelected) {
                        FilledTonalButton(
                            onClick = {},
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = colors.Primary,
                                contentColor   = Color.White
                            )
                        ) { Text(flag, fontSize = 16.sp) }
                    } else {
                        OutlinedButton(
                            onClick = {
                                LanguageManager.setLocale(code)
                                currentLang.value = code
                            },
                            modifier = Modifier.height(36.dp),
                            shape  = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.5.dp, colors.Divider)
                        ) { Text(flag, fontSize = 16.sp) }
                    }
                }
            }

            // Toggle Dark/Light mode
            OutlinedButton(
                onClick = onToggleDarkMode,
                modifier = Modifier.size(40.dp),
                shape  = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, colors.Divider),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Text(
                    text = if (isDarkMode) "☀️" else "🌙",
                    fontSize = 18.sp
                )
            }
        }

        // ── Contenu principal ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "CalcGo Logo",
                modifier = Modifier
                    .size(160.dp)
                    .scale(pulseScale)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text      = stringResource(R.string.app_title),
                fontSize  = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color     = colors.TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 44.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text     = stringResource(R.string.app_subtitle),
                fontSize = 16.sp,
                color    = colors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(56.dp))

            Button(
                onClick  = onPlayClick,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape    = RoundedCornerShape(18.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = colors.Primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text       = stringResource(R.string.btn_play),
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(14.dp))

            OutlinedButton(
                onClick  = onHighscoreClick,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape    = RoundedCornerShape(18.dp),
                border   = BorderStroke(2.dp, colors.Primary),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = colors.Primary)
            ) {
                Text(
                    text       = stringResource(R.string.btn_highscores),
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = colors.Primary
                )
            }
        }
    }
}

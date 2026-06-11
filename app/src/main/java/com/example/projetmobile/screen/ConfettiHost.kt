package com.example.projetmobile.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiPiece(
    val xFraction: Float,       // position X en fraction 0..1
    val startYFraction: Float,  // position Y de départ (peut être négative)
    val speed: Float,           // vitesse de chute 0.2..1.0
    val drift: Float,           // dérive horizontale
    val color: Color,
    val size: Float,            // taille dp-like
    val rotSpeed: Float,        // vitesse de rotation
    val rotOffset: Float        // rotation initiale
)

private val confettiColors = listOf(
    Color(0xFF5B5FEF), Color(0xFFEC4899), Color(0xFFF59E0B),
    Color(0xFF10B981), Color(0xFF6366F1), Color(0xFFF97316),
    Color(0xFF14B8A6), Color(0xFFEF4444), Color(0xFFA855F7)
)

@Composable
fun ConfettiHost(modifier: Modifier = Modifier) {
    val pieces = remember {
        List(90) {
            ConfettiPiece(
                xFraction  = Random.nextFloat(),
                startYFraction = -Random.nextFloat() * 0.8f,
                speed      = 0.25f + Random.nextFloat() * 0.55f,
                drift      = (-0.015f + Random.nextFloat() * 0.03f),
                color      = confettiColors.random(),
                size       = 12f + Random.nextFloat() * 14f,
                rotSpeed   = 1f + Random.nextFloat() * 3f,
                rotOffset  = Random.nextFloat() * 360f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "confetti")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation   = tween(3200, easing = LinearEasing),
            repeatMode  = RepeatMode.Restart
        ),
        label = "confetti_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val W = size.width
        val H = size.height

        pieces.forEach { p ->
            val rawY = p.startYFraction + time * p.speed
            val y = (rawY % 1.2f) * H
            val x = (p.xFraction + time * p.drift + sin(time * 5f + p.rotOffset) * 0.02f)
                .let { ((it % 1f) + 1f) % 1f } * W

            val alpha = when {
                y < 0f    -> 0f
                y > H * 0.85f -> 1f - ((y - H * 0.85f) / (H * 0.15f))
                else      -> 1f
            }.coerceIn(0f, 1f)

            rotate(
                degrees = p.rotOffset + time * p.rotSpeed * 360f,
                pivot   = Offset(x, y)
            ) {
                drawRect(
                    color    = p.color.copy(alpha = alpha),
                    topLeft  = Offset(x - p.size / 2f, y - p.size / 4f),
                    size     = Size(p.size, p.size * 0.5f)
                )
            }
        }
    }
}

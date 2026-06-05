package com.example.projetmobile.screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetmobile.DatabaseHelper
import com.example.projetmobile.R
import kotlin.random.Random

data class MathOperation(
    val a: Int,
    val b: Int,
    val operator: Char,
    val result: Int
)

fun generateOperation(): MathOperation {
    val operator = listOf('+', '-', '×', '÷').random()
    return when (operator) {
        '+' -> { val a = Random.nextInt(1, 100); val b = Random.nextInt(1, 100); MathOperation(a, b, operator, a + b) }
        '-' -> { val a = Random.nextInt(10, 100); val b = Random.nextInt(1, a); MathOperation(a, b, operator, a - b) }
        '×' -> { val a = Random.nextInt(2, 13); val b = Random.nextInt(2, 13); MathOperation(a, b, operator, a * b) }
        '÷' -> { val b = Random.nextInt(2, 13); val r = Random.nextInt(2, 13); MathOperation(b * r, b, operator, r) }
        else -> MathOperation(1, 1, '+', 2)
    }
}

@Composable
fun GameScreen(
    context: Context,
    onGameOver: (Int) -> Unit,   // ← passe le score final
    onMenuClick: () -> Unit
) {
    val colors = AppColors
    val dbHelper = remember { DatabaseHelper(context) }
    var score      by rememberSaveable { mutableIntStateOf(0) }
    var lives      by rememberSaveable { mutableIntStateOf(3) }
    var operation  by remember { mutableStateOf(generateOperation()) }
    var userInput  by rememberSaveable { mutableStateOf("") }
    var feedback   by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var playerName by rememberSaveable { mutableStateOf("") }
    var showQuestion by rememberSaveable { mutableStateOf(true) }
    val keyboard = LocalSoftwareKeyboardController.current

    fun validate() {
        val ans = userInput.trim().toIntOrNull() ?: return
        keyboard?.hide()
        if (ans == operation.result) {
            score += 10; feedback = true; userInput = ""; showQuestion = false
        } else {
            lives -= 1; feedback = false; userInput = ""
            if (lives <= 0) showDialog = true
        }
    }

    LaunchedEffect(feedback) {
        if (feedback != null) {
            kotlinx.coroutines.delay(700)
            feedback = null
            if (lives > 0) { operation = generateOperation(); showQuestion = true }
        }
    }

    // ── Dialogue fin de partie ────────────────────────────────────────────────
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            containerColor   = colors.SurfaceCard,
            shape = RoundedCornerShape(24.dp),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("💀", fontSize = 40.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.game_over_title), color = colors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center)
                }
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(colors.PrimaryLight).padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.game_over_score, score), color = colors.Primary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.enter_name), color = colors.TextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = playerName, onValueChange = { playerName = it },
                        placeholder = { Text(stringResource(R.string.name_placeholder), color = colors.TextHint) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = colors.Primary,
                            unfocusedBorderColor = colors.Divider,
                            focusedTextColor     = colors.TextPrimary,
                            unfocusedTextColor   = colors.TextPrimary,
                            cursorColor          = colors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp), singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = playerName.trim().ifEmpty { context.getString(R.string.anonymous) }
                        dbHelper.insertScore(name, score)
                        showDialog = false
                        onGameOver(score)   // ← score transmis
                    },
                    colors   = ButtonDefaults.buttonColors(containerColor = colors.Primary),
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.save_score), color = Color.White, fontWeight = FontWeight.Bold) }
            }
        )
    }

    // ── Écran de jeu ──────────────────────────────────────────────────────────
    val bgGradient = Brush.linearGradient(
        colors = listOf(colors.Background, colors.Background.copy(alpha = 0.95f)),
        start = Offset(0f, 0f), end = Offset(0f, 1000f)
    )

    Box(modifier = Modifier.fillMaxSize().background(bgGradient)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp)
                .background(Brush.verticalGradient(listOf(colors.PrimaryLight.copy(alpha = 0.4f), Color.Transparent)))
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(52.dp))

            // Top bar
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onMenuClick) {
                    Text("← ${stringResource(R.string.btn_menu)}", color = colors.TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(colors.PrimaryLight).padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.score_label, score), color = colors.Primary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Vies
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                repeat(3) { i -> Text(if (i < lives) "❤️" else "🤍", fontSize = 30.sp, modifier = Modifier.padding(horizontal = 6.dp)) }
            }

            Spacer(Modifier.height(36.dp))

            // Carte question
            AnimatedVisibility(visible = showQuestion, enter = fadeIn(tween(350)) + slideInVertically(tween(350)) { -30 }) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(160.dp).shadow(8.dp, RoundedCornerShape(28.dp)),
                    shape    = RoundedCornerShape(28.dp),
                    colors   = CardDefaults.cardColors(containerColor = colors.SurfaceCard)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${operation.a}  ${operation.operator}  ${operation.b}", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = colors.TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text("= ?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.Primary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Feedback
            feedback?.let { ok ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(14.dp))
                        .background(if (ok) colors.SecondaryLight else colors.ErrorLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (ok) stringResource(R.string.correct) else stringResource(R.string.wrong),
                        color = if (ok) colors.Secondary else colors.Error,
                        fontWeight = FontWeight.Bold, fontSize = 18.sp
                    )
                }
            }

            if (feedback == null) {
                OutlinedTextField(
                    value = userInput, onValueChange = { userInput = it },
                    label = { Text(stringResource(R.string.answer_label), color = colors.TextHint) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { validate() }),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = colors.Primary, unfocusedBorderColor = colors.Divider,
                        focusedTextColor     = colors.TextPrimary, unfocusedTextColor = colors.TextPrimary,
                        focusedLabelColor    = colors.Primary, cursorColor = colors.Primary,
                        unfocusedContainerColor = colors.SurfaceCard, focusedContainerColor = colors.SurfaceCard
                    ),
                    textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = colors.TextPrimary)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick   = { validate() },
                    modifier  = Modifier.fillMaxWidth().height(56.dp),
                    shape     = RoundedCornerShape(18.dp),
                    colors    = ButtonDefaults.buttonColors(containerColor = colors.Primary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(stringResource(R.string.btn_validate), fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

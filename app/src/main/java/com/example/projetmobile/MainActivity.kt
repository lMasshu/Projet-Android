package com.example.projetmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.projetmobile.screen.GameScreen
import com.example.projetmobile.screen.HighscoreScreen
import com.example.projetmobile.screen.LocalIsDark
import com.example.projetmobile.screen.MenuScreen
import com.example.projetmobile.screen.SplashScreen
import com.example.projetmobile.ui.theme.ProjetMobileTheme

enum class AppScreen { SPLASH, MENU, GAME, HIGHSCORE }

// Ordre pour déterminer la direction de la transition
private val screenOrder = listOf(
    AppScreen.SPLASH,
    AppScreen.MENU,
    AppScreen.GAME,
    AppScreen.HIGHSCORE
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetMobileApp()
        }
    }
}

@Composable
fun ProjetMobileApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.SPLASH) }
    var isDarkMode    by rememberSaveable { mutableStateOf(false) }
    var lastScore     by rememberSaveable { mutableIntStateOf(-1) }
    val context = LocalContext.current

    ProjetMobileTheme(dynamicColor = false, darkTheme = isDarkMode) {
        CompositionLocalProvider(LocalIsDark provides isDarkMode) {

            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    val fromIdx = screenOrder.indexOf(initialState)
                    val toIdx   = screenOrder.indexOf(targetState)
                    val forward = toIdx >= fromIdx
                    if (forward) {
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                                (slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()) togetherWith
                                (slideOutHorizontally { it } + fadeOut())
                    }
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.SPLASH -> SplashScreen(
                        onSplashComplete = { currentScreen = AppScreen.MENU }
                    )
                    AppScreen.MENU -> MenuScreen(
                        isDarkMode        = isDarkMode,
                        onToggleDarkMode  = { isDarkMode = !isDarkMode },
                        onPlayClick       = { currentScreen = AppScreen.GAME },
                        onHighscoreClick  = { currentScreen = AppScreen.HIGHSCORE }
                    )
                    AppScreen.GAME -> GameScreen(
                        context     = context,
                        onGameOver  = { score ->
                            lastScore = score
                            currentScreen = AppScreen.HIGHSCORE
                        },
                        onMenuClick = { currentScreen = AppScreen.MENU }
                    )
                    AppScreen.HIGHSCORE -> HighscoreScreen(
                        context     = context,
                        newScore    = lastScore,
                        onMenuClick = { currentScreen = AppScreen.MENU }
                    )
                }
            }
        }
    }
}
package com.example.projetmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.projetmobile.screen.GameScreen
import com.example.projetmobile.screen.HighscoreScreen
import com.example.projetmobile.screen.MenuScreen
import com.example.projetmobile.ui.theme.ProjetMobileTheme

enum class AppScreen {
    MENU, GAME, HIGHSCORE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetMobileTheme(dynamicColor = false, darkTheme = false) {
                ProjetMobileApp()
            }
        }
    }
}

@Composable
fun ProjetMobileApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.MENU) }
    val context = LocalContext.current

    when (currentScreen) {
        AppScreen.MENU -> MenuScreen(
            onPlayClick = { currentScreen = AppScreen.GAME },
            onHighscoreClick = { currentScreen = AppScreen.HIGHSCORE }
        )
        AppScreen.GAME -> GameScreen(
            context = context,
            onGameOver = { currentScreen = AppScreen.HIGHSCORE },
            onMenuClick = { currentScreen = AppScreen.MENU }
        )
        AppScreen.HIGHSCORE -> HighscoreScreen(
            context = context,
            onMenuClick = { currentScreen = AppScreen.MENU }
        )
    }
}
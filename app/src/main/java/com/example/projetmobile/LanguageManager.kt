package com.example.projetmobile

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * Gère le changement de langue de l'application.
 * Utilise AppCompatDelegate.setApplicationLocales() pour changer la langue
 * sans redémarrer l'activité manuellement (API 33+ natif, backport sur versions antérieures).
 */
object LanguageManager {

    const val LANG_FR = "fr"
    const val LANG_EN = "en"

    /**
     * Applique la langue donnée à toute l'application.
     * L'activité est recrée automatiquement par AppCompatDelegate.
     */
    fun setLocale(languageCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    /**
     * Retourne le code langue actuellement actif ("fr" ou "en").
     */
    fun getCurrentLanguage(): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        if (!appLocales.isEmpty) {
            return appLocales[0]?.language ?: LANG_FR
        }
        return Locale.getDefault().language.takeIf { it == LANG_EN } ?: LANG_FR
    }

    /**
     * Bascule entre français et anglais.
     */
    fun toggle() {
        val next = if (getCurrentLanguage() == LANG_FR) LANG_EN else LANG_FR
        setLocale(next)
    }
}

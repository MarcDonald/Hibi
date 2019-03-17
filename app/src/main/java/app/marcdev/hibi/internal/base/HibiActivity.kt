package app.marcdev.hibi.internal.base

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME

abstract class HibiActivity : AppCompatActivity() {
  private var isDarkTheme: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    updateTheme()
  }

  private fun updateTheme() {
    isDarkTheme = if(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false)) {
      setTheme(R.style.Hibi_DarkTheme)
      true
    } else {
      setTheme(R.style.Hibi_LightTheme)
      false
    }
  }

  override fun onResume() {
    super.onResume()
    // Checks if the theme was changed while it was paused and then sees if the current theme of the activity matches
    val isDarkThemeNow = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false)
    if(isDarkTheme != isDarkThemeNow) {
      updateTheme()
      recreate()
    }
  }
}
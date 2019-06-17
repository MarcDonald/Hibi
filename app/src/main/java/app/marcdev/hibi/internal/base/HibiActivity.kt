package app.marcdev.hibi.internal.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.utils.ThemeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

abstract class HibiActivity : AppCompatActivity(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  private val themeUtils: ThemeUtils by instance()
  private var isDarkTheme: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    updateTheme()
  }

  private fun updateTheme() {
    isDarkTheme = if(themeUtils.isDarkMode()) {
      setTheme(R.style.AppTheme_Dark)
      true
    } else {
      setTheme(R.style.AppTheme_Light)
      false
    }
  }

  override fun onResume() {
    super.onResume()
    // Checks if the theme was changed while it was paused and then sees if the current theme of the activity matches
    val isDarkThemeNow = themeUtils.isDarkMode()
    if(isDarkTheme != isDarkThemeNow) {
      updateTheme()
      recreate()
    }
  }
}
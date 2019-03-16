package app.marcdev.hibi

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.hibi.internal.PREF_DARK_THEME
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    if(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false))
      setTheme(R.style.Hibi_DarkTheme)
    else
      setTheme(R.style.Hibi_LightTheme)

    setContentView(R.layout.activity_main)
  }
}

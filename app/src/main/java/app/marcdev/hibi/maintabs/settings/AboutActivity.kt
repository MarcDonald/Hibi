package app.marcdev.hibi.maintabs.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME
import app.marcdev.hibi.internal.base.HibiActivity
import timber.log.Timber

class AboutActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)

    if(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false))
      setTheme(R.style.AppTheme_Dark)
    else
      setTheme(R.style.AppTheme_Light)

    setContentView(R.layout.activity_empty_scroll_with_toolbar)
    bindViews()

    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.scroll_empty, AboutFragment())
    fragmentTransaction.commit()
  }

  private fun bindViews() {
    val toolbarTitle: TextView = findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.about)

    val toolbarBackButton: ImageView = findViewById(R.id.img_back_toolbar_back)
    toolbarBackButton.setOnClickListener(backClickListener)
  }

  private val backClickListener = View.OnClickListener {
    onBackPressed()
  }
}
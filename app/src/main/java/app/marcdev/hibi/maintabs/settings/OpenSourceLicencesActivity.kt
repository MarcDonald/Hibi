package app.marcdev.hibi.maintabs.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME
import app.marcdev.hibi.internal.base.HibiActivity
import app.marcdev.hibi.uicomponents.views.LicenseDisplay
import timber.log.Timber

class OpenSourceLicencesActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)

    if(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false))
      setTheme(R.style.Hibi_DarkTheme)
    else
      setTheme(R.style.Hibi_LightTheme)

    setContentView(R.layout.activity_oss)
    bindViews()
  }

  private fun bindViews() {
    val toolbarTitle: TextView = findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.open_source_licenses)

    val toolbarBackButton: ImageView = findViewById(R.id.img_back_toolbar_back)
    toolbarBackButton.setOnClickListener(backClickListener)

    val timber: LicenseDisplay = findViewById(R.id.license_timber)
    timber.setOnClickListener(openURLClickListener("https://github.com/JakeWharton/timber"))

    val kodein: LicenseDisplay = findViewById(R.id.license_kodein)
    kodein.setOnClickListener(openURLClickListener("https://github.com/Kodein-Framework/Kodein-DI"))

    val retrofit: LicenseDisplay = findViewById(R.id.license_retrofit)
    retrofit.setOnClickListener(openURLClickListener("https://github.com/square/retrofit"))

    val gson: LicenseDisplay = findViewById(R.id.license_gson)
    gson.setOnClickListener(openURLClickListener("https://github.com/google/gson"))

    val androidFilePicker: LicenseDisplay = findViewById(R.id.license_android_file_picker)
    androidFilePicker.setOnClickListener(openURLClickListener("https://github.com/DroidNinja/Android-FilePicker"))

    val mplus: LicenseDisplay = findViewById(R.id.license_mplus)
    mplus.setOnClickListener(openURLClickListener("https://fonts.google.com/specimen/M+PLUS+Rounded+1c"))

    val materialIcons: LicenseDisplay = findViewById(R.id.license_material_icons)
    materialIcons.setOnClickListener(openURLClickListener("https://material.io/tools/icons/"))
  }

  private val backClickListener = View.OnClickListener {
    onBackPressed()
  }

  private fun openURLClickListener(url: String): View.OnClickListener {
    return View.OnClickListener {
      val uriUrl = Uri.parse(url)
      val launchBrowser = Intent(Intent.ACTION_VIEW)
      launchBrowser.data = uriUrl
      startActivity(launchBrowser)
    }
  }
}
package app.marcdev.hibi.maintabs.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.uicomponents.LicenseDisplay
import timber.log.Timber

class OpenSourceLicencesActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_oss)

    bindViews()
  }

  private fun bindViews() {
    val toolbarTitle: TextView = findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.open_source_licenses)

    val toolbarBackButton: ImageView = findViewById(R.id.img_back_toolbar_back)
    toolbarBackButton.setOnClickListener(backClickListener)

    val timber: LicenseDisplay = findViewById(R.id.license_timber)
    timber.setOnClickListener(timberClickListener)

    val kodein: LicenseDisplay = findViewById(R.id.license_kodein)
    kodein.setOnClickListener(kodeinClickListener)

    val retrofit: LicenseDisplay = findViewById(R.id.license_retrofit)
    retrofit.setOnClickListener(retrofitClickListener)

    val gson: LicenseDisplay = findViewById(R.id.license_gson)
    gson.setOnClickListener(gsonClickListener)

    val mplus: LicenseDisplay = findViewById(R.id.license_mplus)
    mplus.setOnClickListener(mplusClickListener)
  }

  private val backClickListener = View.OnClickListener {
    onBackPressed()
  }

  private val timberClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://github.com/JakeWharton/timber")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }

  private val kodeinClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://github.com/Kodein-Framework/Kodein-DI")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }

  private val retrofitClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://github.com/square/retrofit")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }

  private val gsonClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://github.com/google/gson")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }

  private val mplusClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://fonts.google.com/specimen/M+PLUS+Rounded+1c")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }
}
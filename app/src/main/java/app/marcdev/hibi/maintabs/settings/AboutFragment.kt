package app.marcdev.hibi.maintabs.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.hibi.BuildConfig
import app.marcdev.hibi.R
import timber.log.Timber

class AboutFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
    setPreferencesFromResource(R.xml.about, rootKey)

    bindViews()
  }

  private fun bindViews() {
    initVersion()

    val oss = findPreference("about_oss")
    oss.onPreferenceClickListener = ossClickListener

    val jisho = findPreference("about_jisho")
    jisho.onPreferenceClickListener = jishoClickListener

  }

  private fun initVersion() {
    val version = findPreference("about_version")
    version.summary = BuildConfig.VERSION_NAME
    version.setOnPreferenceClickListener {
      val buildCodeString = Integer.toString(BuildConfig.VERSION_CODE)
      Toast.makeText(requireContext(), resources.getString(R.string.build_code_with_output, buildCodeString), Toast.LENGTH_SHORT).show()
      true
    }
  }

  private val ossClickListener = Preference.OnPreferenceClickListener {
    val intent = Intent(requireContext(), OpenSourceLicencesActivity::class.java)
    startActivity(intent)
    true
  }

  private val jishoClickListener = Preference.OnPreferenceClickListener {
    val uriUrl = Uri.parse("https://jisho.org/")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
    true
  }
}
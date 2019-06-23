package com.marcdonald.hibi.mainscreens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.marcdonald.hibi.BuildConfig
import com.marcdonald.hibi.R

class AboutPreferenceFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.about, rootKey)
    bindViews()
  }

  private fun bindViews() {
    initVersion()

    findPreference("about_oss").onPreferenceClickListener = Preference.OnPreferenceClickListener {
      val ossAction = AboutFragmentDirections.ossAction()
      Navigation.findNavController(requireView()).navigate(ossAction)

      true
    }

    findPreference("about_jisho").onPreferenceClickListener = Preference.OnPreferenceClickListener {
      launchURL("https://jisho.org/")
      true
    }

    findPreference("about_privacy").onPreferenceClickListener = Preference.OnPreferenceClickListener {
      val privacyAction = AboutFragmentDirections.privacyAction()
      Navigation.findNavController(requireView()).navigate(privacyAction)
      true
    }

    findPreference("about_code").onPreferenceClickListener = Preference.OnPreferenceClickListener {
      launchURL("https://github.com/MarcDonald/Hibi")
      true
    }

    findPreference("about_author").onPreferenceClickListener = Preference.OnPreferenceClickListener {
      launchURL("https://marcdonald.com")
      true
    }
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

  private fun launchURL(url: String) {
    val uriUrl = Uri.parse(url)
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }
}
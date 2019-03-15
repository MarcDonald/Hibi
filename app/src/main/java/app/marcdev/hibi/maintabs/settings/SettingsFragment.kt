package app.marcdev.hibi.maintabs.settings

import android.os.Bundle
import android.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_ENTRY_DIVIDERS
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
    setPreferencesFromResource(R.xml.preferences, rootKey)

    bindViews()
  }

  private fun bindViews() {
    val mainDivider = findPreference(PREF_ENTRY_DIVIDERS)
    mainDivider.onPreferenceChangeListener = mainDividerChangeListener
  }

  private val mainDividerChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
    Snackbar.make(requireView(), resources.getString(R.string.may_need_restart), Snackbar.LENGTH_SHORT).show()
    true
  }

  private fun matchSummaryToSelection(preference: Preference, value: String) {
    if(preference is ListPreference) {
      val index = preference.findIndexOfValue(value)

      preference.setSummary(
        if(index >= 0) {
          preference.entries[index]
        } else {
          Timber.w("Log: BindPreferenceSummaryToValue: Index < 0")
          null
        })

    } else {
      preference.summary = value
    }
  }
}
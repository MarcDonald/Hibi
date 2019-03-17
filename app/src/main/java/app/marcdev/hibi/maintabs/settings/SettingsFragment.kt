package app.marcdev.hibi.maintabs.settings

import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.text.format.DateFormat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*
import java.util.Calendar.*


class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
    setPreferencesFromResource(R.xml.preferences, rootKey)
    bindViews()
  }

  private fun bindViews() {
    val mainDivider = findPreference(PREF_ENTRY_DIVIDERS)
    mainDivider.onPreferenceChangeListener = mayRequireRestartChangeListener
    val darkTheme = findPreference(PREF_DARK_THEME)
    darkTheme.onPreferenceChangeListener = onThemeChangeListener
    val reminder = findPreference(PREF_REMINDER_NOTIFICATION)
    reminder.onPreferenceChangeListener = reminderChangeListener
  }

  private val onThemeChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
    requireActivity().recreate()
    true
  }

  private val reminderChangeListener = Preference.OnPreferenceChangeListener { pref, isActive ->
    val helper = NotificationHelper()
    if(isActive == true) {
      // Sets the 11pm time as the alarm time
      val calendar = Calendar.getInstance()
      calendar.set(HOUR_OF_DAY, 23)
      calendar.set(MINUTE, 0)
      calendar.set(SECOND, 0)
      PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putLong(PREF_REMINDER_TIME, calendar.timeInMillis).apply()

      val timePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmm")
      val formattedTime = DateFormat.format(timePattern, calendar) as String
      matchSummaryToSelection(findPreference(PREF_REMINDER_TIME), formattedTime)

      helper.startAlarm(requireContext())
    } else {
      findPreference(PREF_REMINDER_TIME).summary = resources.getString(R.string.reminder_not_set)
      helper.cancelAlarm(requireContext())
    }
    true
  }

  private val mayRequireRestartChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
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
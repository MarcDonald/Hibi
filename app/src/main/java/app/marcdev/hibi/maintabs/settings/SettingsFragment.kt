package app.marcdev.hibi.maintabs.settings

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.text.format.DateFormat
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.hibi.R
import app.marcdev.hibi.data.BackupUtils
import app.marcdev.hibi.internal.*
import app.marcdev.hibi.internal.base.BinaryOptionDialog
import app.marcdev.hibi.uicomponents.ReminderTimePickerDialog
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*
import java.util.Calendar.*


class SettingsFragment : PreferenceFragmentCompat(), KodeinAware {
  // Kodein initialisation
  override val kodein by closestKodein()

  private val backupUtils: BackupUtils by instance()

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
    if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_REMINDER_NOTIFICATION, false)) {
      displayReminderTimeSummary()
    }
    val reminderTime = findPreference(PREF_REMINDER_TIME)
    reminderTime.onPreferenceClickListener = reminderTimeClickListener
    PreferenceManager.getDefaultSharedPreferences(requireContext()).registerOnSharedPreferenceChangeListener(reminderTimeChangeListener)

    val backup = findPreference(PREF_BACKUP)
    backup.onPreferenceClickListener = backupClickListener

    val restore = findPreference(PREF_RESTORE)
    restore.onPreferenceClickListener = restoreClickListener
  }

  private val onThemeChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
    requireActivity().recreate()
    true
  }

  private fun displayReminderTimeSummary(calendar: Calendar) {
    val timePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmm")
    val formattedTime = DateFormat.format(timePattern, calendar) as String
    matchSummaryToSelection(findPreference(PREF_REMINDER_TIME), formattedTime)
  }

  private fun displayReminderTimeSummary() {
    val calendar = Calendar.getInstance()
    val currentSetAlarmTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
    calendar.timeInMillis = currentSetAlarmTime
    displayReminderTimeSummary(calendar)
  }

  private val reminderChangeListener = Preference.OnPreferenceChangeListener { pref, isActive ->
    val helper = NotificationHelper()
    if(isActive == true) {
      val calendar = Calendar.getInstance()
      val currentSetAlarmTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
      if(currentSetAlarmTime == 0L) {
        // Sets the 11pm time as the alarm time
        calendar.set(HOUR_OF_DAY, 23)
        calendar.set(MINUTE, 0)
        calendar.set(SECOND, 0)
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putLong(PREF_REMINDER_TIME, calendar.timeInMillis).apply()
      } else {
        calendar.timeInMillis = currentSetAlarmTime
      }

      displayReminderTimeSummary(calendar)
      helper.startAlarm(requireContext())
    } else {
      findPreference(PREF_REMINDER_TIME).summary = resources.getString(R.string.reminder_not_set)
      helper.cancelAlarm(requireContext())
    }
    true
  }

  private val reminderTimeChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
    if(prefs.getBoolean(PREF_REMINDER_NOTIFICATION, false))
      displayReminderTimeSummary()
    else
      matchSummaryToSelection(findPreference(PREF_REMINDER_TIME), resources.getString(R.string.reminder_not_set))
  }

  private val reminderTimeClickListener = Preference.OnPreferenceClickListener {
    val dialog = ReminderTimePickerDialog()
    dialog.show(requireFragmentManager(), "Reminder Time Picker Dialog")
    true
  }

  private val backupClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      val backup = backupUtils.backup(requireContext())
      if(backup)
        Snackbar.make(requireView(), resources.getString(R.string.backup_success), Snackbar.LENGTH_SHORT).show()
      else
        Snackbar.make(requireView(), resources.getString(R.string.backup_fail), Snackbar.LENGTH_SHORT).show()
    }
    true
  }

  private val restoreClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      val dialog = BinaryOptionDialog()
      dialog.setTitle(resources.getString(R.string.warning_caps))
      dialog.setMessage(resources.getString(R.string.restore_warning))
      dialog.setPositiveButton(resources.getString(R.string.cancel), View.OnClickListener { dialog.dismiss() })
      dialog.setNegativeButton(resources.getString(R.string.restore_confirm), View.OnClickListener {
        dialog.dismiss()
        val restore = backupUtils.restore(requireContext())
        if(restore) {
          /* This is apparently bad practice but I can't find any other way of completely destroying
           * the application so that the database can be opened again */
          System.exit(1)
        } else {
          Snackbar.make(requireView(), resources.getString(R.string.restore_fail), Snackbar.LENGTH_LONG).show()
        }
      })
      dialog.show(requireFragmentManager(), "Restore Confirm Dialog")
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

  override fun onDestroy() {
    super.onDestroy()
    PreferenceManager.getDefaultSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(reminderTimeChangeListener)
  }
}
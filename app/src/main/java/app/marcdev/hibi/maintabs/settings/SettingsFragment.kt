package app.marcdev.hibi.maintabs.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.format.DateFormat
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.*
import app.marcdev.hibi.maintabs.settings.backupdialog.BackupDialog
import app.marcdev.hibi.maintabs.settings.restoredialog.RestoreDialog
import app.marcdev.hibi.uicomponents.TimePickerDialog
import com.google.android.material.snackbar.Snackbar
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber
import java.util.*
import java.util.Calendar.*


class SettingsFragment : PreferenceFragmentCompat(), KodeinAware {
  // Kodein initialisation
  override val kodein by closestKodein()

  // <editor-fold desc="UI Components">
  private lateinit var reminderTimePickerDialog: TimePickerDialog
  // </editor-fold>

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
    setPreferencesFromResource(R.xml.preferences, rootKey)
    bindViews()
  }

  private fun bindViews() {
    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

    val mainDivider = findPreference(PREF_ENTRY_DIVIDERS)
    mainDivider.onPreferenceChangeListener = mayRequireRestartChangeListener

    val darkTheme = findPreference(PREF_DARK_THEME)
    darkTheme.onPreferenceChangeListener = onThemeChangeListener

    val reminder = findPreference(PREF_REMINDER_NOTIFICATION)
    reminder.onPreferenceChangeListener = reminderChangeListener
    if(sharedPreferences.getBoolean(PREF_REMINDER_NOTIFICATION, false)) {
      displayReminderTimeSummary()
    }
    val reminderTime = findPreference(PREF_REMINDER_TIME)
    reminderTime.onPreferenceClickListener = reminderTimeClickListener
    sharedPreferences.registerOnSharedPreferenceChangeListener(reminderTimeChangeListener)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    matchSummaryToSelection(findPreference(PREF_CLIPBOARD_BEHAVIOR), sharedPreferences.getString(PREF_CLIPBOARD_BEHAVIOR, "0"))
    sharedPreferences.registerOnSharedPreferenceChangeListener(clipboardBehaviorChangeListener)

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
    val calendar = getInstance()
    val currentSetAlarmTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
    calendar.timeInMillis = currentSetAlarmTime
    displayReminderTimeSummary(calendar)
  }

  private val reminderChangeListener = Preference.OnPreferenceChangeListener { _, isActive ->
    val helper = NotificationHelper()
    if(isActive == true) {
      val calendar = getInstance()
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

  private val clipboardBehaviorChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    matchSummaryToSelection(findPreference(PREF_CLIPBOARD_BEHAVIOR), prefs.getString(PREF_CLIPBOARD_BEHAVIOR, "0"))
  }

  private val reminderTimeClickListener = Preference.OnPreferenceClickListener {
    val currentSetTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
    val calendar = getInstance()
    calendar.timeInMillis = currentSetTime
    val hour = calendar.get(HOUR_OF_DAY)
    val minute = calendar.get(MINUTE)

    reminderTimePickerDialog = TimePickerDialog.Builder()
      .setOkClickListener(reminderTimeDialogOkClickListener)
      .setCancelClickListener(reminderTimeDialogCancelClickListener)
      .initTimePicker(hour, minute, null)
      .build()
    reminderTimePickerDialog.show(requireFragmentManager(), "Reminder Time Picker Dialog")
    true
  }

  private val reminderTimeDialogOkClickListener = View.OnClickListener {
    val calendar = getInstance()
    calendar.set(HOUR_OF_DAY, reminderTimePickerDialog.hour)
    calendar.set(MINUTE, reminderTimePickerDialog.minute)
    calendar.set(SECOND, 0)
    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putLong(PREF_REMINDER_TIME, calendar.timeInMillis).apply()
    val helper = NotificationHelper()
    // Cancel previously set alarm and set the new one with the new time
    helper.cancelAlarm(requireContext())
    helper.startAlarm(requireContext())
    reminderTimePickerDialog.dismiss()
  }

  private val reminderTimeDialogCancelClickListener = View.OnClickListener {
    reminderTimePickerDialog.dismiss()
  }

  private val backupClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      val dialog = BackupDialog()
      dialog.show(requireFragmentManager(), "Backup Dialog")
    }
    true
  }

  private val restoreClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      FilePickerBuilder.instance
        .setMaxCount(1)
        .setActivityTheme(R.style.AppTheme_Dark)
        .setActivityTitle(resources.getString(R.string.restore_title))
        .enableDocSupport(false)
        .addFileSupport(resources.getString(R.string.backup_file), Array(1) { ".hibi" })
        .pickFile(this, CHOOSE_RESTORE_FILE_REQUEST_CODE)
    }
    true
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if(requestCode == CHOOSE_RESTORE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      if(data != null) {
        val filePathArray = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
        val filePath = filePathArray[0]
        if(filePath != null)
          displayRestoreDialog(filePath)
      }
    } else
      super.onActivityResult(requestCode, resultCode, data)
  }

  private fun displayRestoreDialog(path: String) {
    val dialog = RestoreDialog()
    val bundle = Bundle()
    bundle.putString(RESTORE_FILE_PATH_KEY, path)
    dialog.arguments = bundle
    dialog.show(requireFragmentManager(), "Restore Dialog")
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
          Timber.w("Log: matchSummaryToSelection: Index < 0")
          null
        })

    } else {
      preference.summary = value
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    PreferenceManager.getDefaultSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(reminderTimeChangeListener)
    PreferenceManager.getDefaultSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(clipboardBehaviorChangeListener)
  }
}
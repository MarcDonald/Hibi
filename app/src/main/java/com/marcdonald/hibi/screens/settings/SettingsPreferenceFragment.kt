/*
 * Copyright 2021 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.screens.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.*
import com.marcdonald.hibi.notification.ReminderAlertReceiver
import com.marcdonald.hibi.screens.settings.backupdialog.BackupDialog
import com.marcdonald.hibi.screens.settings.restoredialog.RestoreDialog
import com.marcdonald.hibi.screens.settings.updatedialog.UpdateDialog
import com.marcdonald.hibi.uicomponents.TimePickerDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber
import java.util.*
import java.util.Calendar.*

class SettingsPreferenceFragment : PreferenceFragmentCompat(), KodeinAware {

	// Kodein initialisation
	override val kodein by closestKodein()

	// <editor-fold desc="UI Components">
	private lateinit var reminderTimePickerDialog: TimePickerDialog
	// </editor-fold>

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences, rootKey)
		bindViews()
	}

	private fun bindViews() {
		val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

		setupNightModePref()

		findPreference<Preference>(PREF_REMINDER_NOTIFICATION)?.onPreferenceChangeListener = reminderChangeListener
		if(sharedPreferences.getBoolean(PREF_REMINDER_NOTIFICATION, false)) {
			displayReminderTimeSummary()
		}
		findPreference<Preference>(PREF_REMINDER_TIME)?.onPreferenceClickListener = reminderTimeClickListener
		sharedPreferences.registerOnSharedPreferenceChangeListener(reminderTimeChangeListener)

		val clipboardBehaviorPreference = findPreference<Preference>(PREF_CLIPBOARD_BEHAVIOR)
		clipboardBehaviorPreference?.let {
			(sharedPreferences.getString(PREF_CLIPBOARD_BEHAVIOR, "0")?.let { value -> matchSummaryToSelection(clipboardBehaviorPreference, value) })
			sharedPreferences.registerOnSharedPreferenceChangeListener(clipboardBehaviorChangeListener)
		}

		val dateHeaderPeriodPreference = findPreference<Preference>(PREF_DATE_HEADER_PERIOD)
		dateHeaderPeriodPreference?.let {
			(sharedPreferences.getString(PREF_DATE_HEADER_PERIOD, "1")?.let { value -> matchSummaryToSelection(dateHeaderPeriodPreference, value) })
			sharedPreferences.registerOnSharedPreferenceChangeListener(dateHeaderPeriodChangeListener)
		}

		findPreference<Preference>(PREF_BACKUP)?.onPreferenceClickListener = backupClickListener
		findPreference<Preference>(PREF_RESTORE)?.onPreferenceClickListener = restoreClickListener

		val mainEntryDisplayItems = findPreference<Preference>(PREF_MAIN_ENTRY_DISPLAY_ITEMS)
		mainEntryDisplayItems?.onPreferenceClickListener = mainEntryDisplayItemsClickListener

		findPreference<Preference>("pref_app_update")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			val dialog = UpdateDialog()
			dialog.show(parentFragmentManager, "Update Dialog")
			true
		}
	}

	private fun setupNightModePref() {
		val nightModePref = findPreference<DropDownPreference>(PREF_NIGHT_MODE)
		nightModePref?.let {
			nightModePref.apply {
				entries = arrayOf(resources.getString(R.string.follow_system), resources.getString(R.string.always_day), resources.getString(R.string.always_night))
				entryValues = arrayOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString(), AppCompatDelegate.MODE_NIGHT_NO.toString(), AppCompatDelegate.MODE_NIGHT_YES.toString())
				summary = getNightModeSummary(AppCompatDelegate.getDefaultNightMode())
			}
			nightModePref.setOnPreferenceChangeListener { pref, newValue ->
				PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putString(PREF_NIGHT_MODE, newValue as String).apply()
				AppCompatDelegate.setDefaultNightMode(newValue.toInt())
				pref.summary = getNightModeSummary(newValue.toInt())
				true
			}
		}
	}

	private fun getNightModeSummary(mode: Int): String {
		return when(mode) {
			AppCompatDelegate.MODE_NIGHT_YES -> resources.getString(R.string.always_night)
			AppCompatDelegate.MODE_NIGHT_NO -> resources.getString(R.string.always_day)
			else                             -> resources.getString(R.string.follow_system)
		}
	}

	private fun displayReminderTimeSummary(calendar: Calendar) {
		val timePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmm")
		val formattedTime = DateFormat.format(timePattern, calendar) as String
		val reminderTimePreference = findPreference<Preference>(PREF_REMINDER_TIME)
		reminderTimePreference?.let {
			matchSummaryToSelection(reminderTimePreference, formattedTime)
		}
	}

	private fun displayReminderTimeSummary() {
		val calendar = getInstance()
		val currentSetAlarmTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
		calendar.timeInMillis = currentSetAlarmTime
		displayReminderTimeSummary(calendar)
	}

	private val reminderChangeListener = Preference.OnPreferenceChangeListener { _, isActive ->
		val reminderAlertReceiver = ReminderAlertReceiver()
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
			reminderAlertReceiver.startAlarm(requireContext())
		} else {
			findPreference<Preference>(PREF_REMINDER_TIME)?.summary = resources.getString(R.string.reminder_not_set)
			reminderAlertReceiver.cancelAlarm(requireContext())
		}
		true
	}

	private val reminderTimeChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
		if(prefs.getBoolean(PREF_REMINDER_NOTIFICATION, false))
			displayReminderTimeSummary()
		else {
			val reminderTimePreference = findPreference<Preference>(PREF_REMINDER_TIME)
			reminderTimePreference?.let {
				matchSummaryToSelection(reminderTimePreference, resources.getString(R.string.reminder_not_set))
			}
		}
	}

	private val clipboardBehaviorChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
		val clipboardBehaviorPreference = findPreference<Preference>(PREF_CLIPBOARD_BEHAVIOR)
		clipboardBehaviorPreference?.let {
			(prefs.getString(PREF_CLIPBOARD_BEHAVIOR, "0")?.let { value -> matchSummaryToSelection(clipboardBehaviorPreference, value) })
		}
	}

	private val dateHeaderPeriodChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, _ ->
		val dateHeaderPeriodPreference = findPreference<Preference>(PREF_DATE_HEADER_PERIOD)
		dateHeaderPeriodPreference?.let {
			prefs.getString(PREF_DATE_HEADER_PERIOD, "1")?.let { value -> matchSummaryToSelection(dateHeaderPeriodPreference, value) }
		}
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
		reminderTimePickerDialog.show(parentFragmentManager, "Reminder Time Picker Dialog")
		true
	}

	private val reminderTimeDialogOkClickListener = View.OnClickListener {
		val reminderAlertReceiver = ReminderAlertReceiver()
		val calendar = getInstance()
		calendar.set(HOUR_OF_DAY, reminderTimePickerDialog.hour)
		calendar.set(MINUTE, reminderTimePickerDialog.minute)
		calendar.set(SECOND, 0)
		PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putLong(PREF_REMINDER_TIME, calendar.timeInMillis).apply()
		// Cancel previously set alarm and set the new one with the new time
		reminderAlertReceiver.cancelAlarm(requireContext())
		reminderAlertReceiver.startAlarm(requireContext())
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
			dialog.show(parentFragmentManager, "Backup Dialog")
		}
		true
	}

	private val restoreClickListener = Preference.OnPreferenceClickListener {
		if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
		} else {
			FilePickerBuilder.instance
				.setMaxCount(1)
				.setActivityTheme(R.style.AppTheme)
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
				val filePath = filePathArray?.get(0)
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
		dialog.show(parentFragmentManager, "Restore Dialog")
	}

	private val mainEntryDisplayItemsClickListener = Preference.OnPreferenceClickListener {
		val dialog = MainEntriesDisplayPreferenceDialog()
		dialog.show(parentFragmentManager, "Main Entries Display Preference Dialog")
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
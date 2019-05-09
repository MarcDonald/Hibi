package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.NotificationHelper
import app.marcdev.hibi.internal.PREF_REMINDER_TIME
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber
import java.util.Calendar.*

class ReminderTimePickerDialog : HibiDialogFragment() {

  // <editor-fold desc="UI Components">
  private lateinit var timePicker: TimePicker
  private lateinit var cancelButton: MaterialButton
  private lateinit var okButton: MaterialButton
  // </editor-fold>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_timepicker, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    this.timePicker = view.findViewById(R.id.timepicker)
    timePicker.setIs24HourView(true)

    val currentSetTime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getLong(PREF_REMINDER_TIME, 0)
    val calendar = getInstance()
    calendar.timeInMillis = currentSetTime
    timePicker.hour = calendar.get(HOUR_OF_DAY)
    timePicker.minute = calendar.get(MINUTE)

    cancelButton = view.findViewById(R.id.btn_timepicker_cancel)
    cancelButton.setOnClickListener(cancelClickListener)

    okButton = view.findViewById(R.id.btn_timepicker_ok)
    okButton.setOnClickListener(okClickListener)
  }

  private val cancelClickListener = View.OnClickListener {
    dismiss()
  }

  private val okClickListener = View.OnClickListener {
    val calendar = getInstance()
    calendar.set(HOUR_OF_DAY, timePicker.hour)
    calendar.set(MINUTE, timePicker.minute)
    calendar.set(SECOND, 0)
    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putLong(PREF_REMINDER_TIME, calendar.timeInMillis).apply()
    val helper = NotificationHelper()
    // Cancel previously set alarm and set the new one with the new time
    helper.cancelAlarm(requireContext())
    helper.startAlarm(requireContext())
    dismiss()
  }
}
package app.marcdev.hibi.entryscreens.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class EntryTimePickerDialog : HibiDialogFragment() {
  // Date/Time Store
  private var dateTimeStore: DateTimeStore? = null

  // UI Components
  private lateinit var timePicker: TimePicker

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_timepicker, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    this.timePicker = view.findViewById(R.id.timepicker)
    timePicker.setIs24HourView(true)
    if(dateTimeStore != null) {
      timePicker.hour = dateTimeStore!!.getHour()
      timePicker.minute = dateTimeStore!!.getMinute()
    } else {
      Timber.e("Log: bindViews: dateTimeStore is null")
    }

    val cancelButton: MaterialButton = view.findViewById(R.id.btn_timepicker_cancel)
    cancelButton.setOnClickListener(cancelOnClickListener)

    val okButton: MaterialButton = view.findViewById(R.id.btn_timepicker_ok)
    okButton.setOnClickListener(okOnClickListener)
  }

  private val cancelOnClickListener = View.OnClickListener {
    dismiss()
  }

  private val okOnClickListener = View.OnClickListener {
    val hour = timePicker.hour
    val minute = timePicker.minute

    dateTimeStore?.setTime(hour, minute)
    dismiss()
  }

  fun bindDateTimeStore(dateTimeStore: DateTimeStore) {
    this.dateTimeStore = dateTimeStore
  }
}
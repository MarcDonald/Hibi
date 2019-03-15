package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import app.marcdev.hibi.R
import app.marcdev.hibi.entryscreens.addentryscreen.DateTimeStore
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class DatePickerDialog : HibiDialogFragment() {
  // Date/Time Store
  private var dateTimeStore: DateTimeStore? = null

  // UI Components
  private lateinit var datePicker: DatePicker

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_datepicker, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    this.datePicker = view.findViewById(R.id.datepicker)
    if(dateTimeStore != null) {
      datePicker.init(dateTimeStore!!.getYear(), dateTimeStore!!.getMonth(), dateTimeStore!!.getDay(), null)
    } else {
      Timber.e("Log: bindViews: dateTimeStore is null")
    }

    val cancelButton: MaterialButton = view.findViewById(R.id.btn_datepicker_cancel)
    cancelButton.setOnClickListener(cancelOnClickListener)

    val okButton: MaterialButton = view.findViewById(R.id.btn_datepicker_ok)
    okButton.setOnClickListener(okOnClickListener)
  }

  private val cancelOnClickListener = View.OnClickListener {
    dismiss()
  }

  private val okOnClickListener = View.OnClickListener {
    val day = datePicker.dayOfMonth
    val month = datePicker.month
    val year = datePicker.year

    dateTimeStore?.setDate(day, month, year)
    dismiss()
  }

  fun bindDateTimeStore(dateTimeStore: DateTimeStore) {
    this.dateTimeStore = dateTimeStore
  }
}
package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import app.marcdev.hibi.internal.extension.show
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class DatePickerDialog : HibiDialogFragment() {

  // <editor-fold desc="UI Components">
  private lateinit var datePicker: DatePicker
  private lateinit var okButton: MaterialButton
  private lateinit var cancelButton: MaterialButton
  private lateinit var extraButton: MaterialButton
  // </editor-fold>

  // <editor-fold desc="To Set">
  private var okClickListenerToSet: View.OnClickListener? = null
  private var cancelClickListenerToSet: View.OnClickListener? = null
  private var datePickerDateChangedListenerToSet: DatePicker.OnDateChangedListener? = null
  private var datePickerYearToSet: Int? = null
  private var datePickerMonthToSet: Int? = null
  private var datePickerDayToSet: Int? = null
  private var showExtraButtonToSet = false
  private var extraButtonTextToSet = ""
  private var extraButtonClickListener: View.OnClickListener? = null
  // </editor-fold>

  val year: Int
    get() = datePicker.year

  val month: Int
    get() = datePicker.month

  val day: Int
    get() = datePicker.dayOfMonth

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_datepicker, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    datePicker = view.findViewById(R.id.datepicker)
    if(datePickerYearToSet != null && datePickerMonthToSet != null && datePickerDayToSet != null)
      datePicker.init(datePickerYearToSet!!, datePickerMonthToSet!!, datePickerDayToSet!!, datePickerDateChangedListenerToSet)

    cancelButton = view.findViewById(R.id.btn_datepicker_cancel)
    cancelButton.setOnClickListener(cancelClickListenerToSet)

    okButton = view.findViewById(R.id.btn_datepicker_ok)
    okButton.setOnClickListener(okClickListenerToSet)

    extraButton = view.findViewById(R.id.btn_datepicker_extra)
    extraButton.text = extraButtonTextToSet
    extraButton.setOnClickListener(extraButtonClickListener)
    extraButton.show(showExtraButtonToSet)
  }

  private fun setOkClickListener(listener: View.OnClickListener) {
    okClickListenerToSet = listener
  }

  private fun setCancelClickListener(listener: View.OnClickListener) {
    cancelClickListenerToSet = listener
  }

  private fun initDatePicker(year: Int, month: Int, day: Int, onDateChangedListener: DatePicker.OnDateChangedListener?) {
    datePickerYearToSet = year
    datePickerMonthToSet = month
    datePickerDayToSet = day
    datePickerDateChangedListenerToSet = onDateChangedListener
  }

  private fun showExtraButton(show: Boolean, text: String, clickListener: View.OnClickListener?) {
    showExtraButtonToSet = show
    extraButtonTextToSet = text
    extraButtonClickListener = clickListener
  }

  class Builder {
    private var _cancelClickListener: View.OnClickListener? = null
    private var _okClickListener: View.OnClickListener? = null
    private var _onDateChangedListener: DatePicker.OnDateChangedListener? = null
    private var _year: Int? = null
    private var _month: Int? = null
    private var _day: Int? = null
    private var _showExtraButton = false
    private var _extraButtonText: String = ""
    private var _extraButtonClickListener: View.OnClickListener? = null

    fun setOkClickListener(listener: View.OnClickListener): Builder {
      _okClickListener = listener
      return this
    }

    fun setCancelClickListener(listener: View.OnClickListener): Builder {
      _cancelClickListener = listener
      return this
    }

    fun initDatePicker(year: Int, month: Int, day: Int, onDateChangedListener: DatePicker.OnDateChangedListener?): Builder {
      _year = year
      _month = month
      _day = day
      _onDateChangedListener = onDateChangedListener
      return this
    }

    fun showExtraButton(text: String, clickListener: View.OnClickListener): Builder {
      _extraButtonText = text
      _extraButtonClickListener = clickListener
      _showExtraButton = true
      return this
    }

    fun build(): DatePickerDialog {
      val dialog = DatePickerDialog()
      if(_okClickListener != null)
        dialog.setOkClickListener(_okClickListener!!)
      if(_cancelClickListener != null)
        dialog.setCancelClickListener(_cancelClickListener!!)
      if(_year != null && _month != null && _day != null)
        dialog.initDatePicker(_year!!, _month!!, _day!!, _onDateChangedListener)
      dialog.showExtraButton(_showExtraButton, _extraButtonText, _extraButtonClickListener)
      return dialog
    }
  }
}
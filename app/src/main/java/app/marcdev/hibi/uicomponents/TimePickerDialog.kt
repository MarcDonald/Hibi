package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class TimePickerDialog : HibiDialogFragment() {

  // <editor-fold desc="UI Components">
  private lateinit var timePicker: TimePicker
  private lateinit var okButton: MaterialButton
  private lateinit var cancelButton: MaterialButton
  private lateinit var extraButton: MaterialButton
  // </editor-fold>

  // <editor-fold desc="To Set">
  private var okClickListenerToSet: View.OnClickListener? = null
  private var cancelClickListenerToSet: View.OnClickListener? = null
  private var timePickerTimeChangedListenerToSet: TimePicker.OnTimeChangedListener? = null
  private var timePickerHourToSet: Int? = null
  private var timePickerMinuteToSet: Int? = null
  private var _is24Hour: Boolean = true
  private var showExtraButtonToSet = false
  private var extraButtonTextToSet = ""
  private var extraButtonClickListener: View.OnClickListener? = null

  // </editor-fold>

  val hour: Int
    get() = timePicker.hour

  val minute: Int
    get() = timePicker.minute

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_timepicker, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    timePicker = view.findViewById(R.id.timepicker)
    if(timePickerHourToSet != null)
      timePicker.hour = timePickerHourToSet!!
    if(timePickerMinuteToSet != null)
      timePicker.minute = timePickerMinuteToSet!!
    timePicker.setOnTimeChangedListener(timePickerTimeChangedListenerToSet)
    timePicker.setIs24HourView(_is24Hour)

    cancelButton = view.findViewById(R.id.btn_timepicker_cancel)
    cancelButton.setOnClickListener(cancelClickListenerToSet)

    okButton = view.findViewById(R.id.btn_timepicker_ok)
    okButton.setOnClickListener(okClickListenerToSet)

    extraButton = view.findViewById(R.id.btn_timepicker_extra)
    extraButton.text = extraButtonTextToSet
    extraButton.setOnClickListener(extraButtonClickListener)
    extraButton.visibility = if(showExtraButtonToSet) View.VISIBLE else View.GONE
  }

  private fun setOkClickListener(listener: View.OnClickListener) {
    okClickListenerToSet = listener
  }

  private fun setCancelClickListener(listener: View.OnClickListener) {
    cancelClickListenerToSet = listener
  }

  private fun initTimePicker(hour: Int, minute: Int, onTimeChangedListener: TimePicker.OnTimeChangedListener?) {
    timePickerHourToSet = hour
    timePickerMinuteToSet = minute
    timePickerTimeChangedListenerToSet = onTimeChangedListener
  }

  private fun setIs24HourView(is24Hour: Boolean) {
    _is24Hour = is24Hour
  }

  private fun showExtraButton(show: Boolean, text: String, clickListener: View.OnClickListener?) {
    showExtraButtonToSet = show
    extraButtonTextToSet = text
    extraButtonClickListener = clickListener
  }

  class Builder {
    private var _cancelClickListener: View.OnClickListener? = null
    private var _okClickListener: View.OnClickListener? = null
    private var _onTimeChangedListener: TimePicker.OnTimeChangedListener? = null
    private var _hour: Int? = null
    private var _minute: Int? = null
    private var _is24Hour: Boolean = true
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

    fun initTimePicker(hour: Int, minute: Int, onTimeChangedListener: TimePicker.OnTimeChangedListener?): Builder {
      _hour = hour
      _minute = minute
      _onTimeChangedListener = onTimeChangedListener
      return this
    }

    fun setIs24HourView(is24Hour: Boolean): Builder {
      _is24Hour = is24Hour
      return this
    }

    fun showExtraButton(text: String, clickListener: View.OnClickListener): Builder {
      _extraButtonText = text
      _extraButtonClickListener = clickListener
      _showExtraButton = true
      return this
    }

    fun build(): TimePickerDialog {
      val dialog = TimePickerDialog()
      if(_okClickListener != null)
        dialog.setOkClickListener(_okClickListener!!)
      if(_cancelClickListener != null)
        dialog.setCancelClickListener(_cancelClickListener!!)
      if(_hour != null && _minute != null)
        dialog.initTimePicker(_hour!!, _minute!!, _onTimeChangedListener)
      dialog.setIs24HourView(_is24Hour)
      dialog.showExtraButton(_showExtraButton, _extraButtonText, _extraButtonClickListener)
      return dialog
    }
  }
}
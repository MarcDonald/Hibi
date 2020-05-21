/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show
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
		extraButton.show(showExtraButtonToSet)
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
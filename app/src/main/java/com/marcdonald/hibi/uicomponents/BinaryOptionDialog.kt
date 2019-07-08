/*
 * Copyright 2019 Marc Donald
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
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show

class BinaryOptionDialog : HibiDialogFragment() {

	// <editor-fold desc="UI Components">
	private lateinit var negativeButton: MaterialButton
	private lateinit var positiveButton: MaterialButton
	private lateinit var titleDisplay: TextView
	private lateinit var messageDisplay: TextView
	// </editor-fold>

	// <editor-fold desc="To Set">
	private var negativeButtonText = ""
	private var positiveButtonText = ""
	private var titleText = ""
	private var messageText = ""
	private var negativeButtonClickListener: View.OnClickListener? = null
	private var positiveButtonClickListener: View.OnClickListener? = null
	private var isTitleVisible = true
	private var isMessageVisible = true
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_binary_option, container, false)
		bindViews(view)
		setContent()
		return view
	}

	private fun bindViews(view: View) {
		negativeButton = view.findViewById(R.id.btn_binary_dialog_negative)
		positiveButton = view.findViewById(R.id.btn_binary_dialog_positive)

		titleDisplay = view.findViewById(R.id.txt_binary_dialog_title)
		messageDisplay = view.findViewById(R.id.txt_binary_dialog_message)
	}

	private fun setContent() {
		if(titleText.isNotBlank()) {
			titleDisplay.text = titleText
		}

		if(messageText.isNotBlank()) {
			messageDisplay.text = messageText
		}

		if(negativeButtonText.isNotBlank()) {
			negativeButton.text = negativeButtonText
		}

		if(positiveButtonText.isNotBlank()) {
			positiveButton.text = positiveButtonText
		}

		if(negativeButtonClickListener != null) {
			negativeButton.setOnClickListener(negativeButtonClickListener)
		} else {
			negativeButton.setOnClickListener(defaultClickListener)
		}

		if(positiveButtonClickListener != null) {
			positiveButton.setOnClickListener(positiveButtonClickListener)
		} else {
			positiveButton.setOnClickListener(defaultClickListener)
		}

		titleDisplay.show(isTitleVisible)
		messageDisplay.show(isMessageVisible)
	}

	fun setNegativeButton(text: String, clickListener: View.OnClickListener) {
		negativeButtonText = text
		negativeButtonClickListener = clickListener
	}

	fun setPositiveButton(text: String, clickListener: View.OnClickListener) {
		positiveButtonText = text
		positiveButtonClickListener = clickListener
	}

	fun setTitle(text: String) {
		if(view != null) {
			titleDisplay.text = text
		} else {
			titleText = text
		}
	}

	fun setMessage(text: String) {
		if(view != null) {
			messageDisplay.text = text
		} else {
			messageText = text
		}
	}

	fun setTitleVisibility(isVisible: Boolean) {
		isTitleVisible = isVisible
	}

	fun setMessageVisiblity(isVisible: Boolean) {
		isMessageVisible = isVisible
	}

	private val defaultClickListener = View.OnClickListener {
		dismiss()
	}
}
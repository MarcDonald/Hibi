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
package com.marcdonald.hibi.screens.settings.restoredialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.RESTORE_FILE_PATH_KEY
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show

class RestoreDialog : HibiDialogFragment() {

	private val viewModel by viewModels<RestoreDialogViewModel> { viewModelFactory }
	// <editor-fold desc="UI Components">
	private lateinit var loadingProgressBar: ProgressBar
	private lateinit var cancelButton: MaterialButton
	private lateinit var messageDisplay: TextView
	private lateinit var confirmButton: MaterialButton
	private lateinit var dismissButton: MaterialButton
	private lateinit var title: TextView
	private lateinit var errorDisplay: ImageView
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_restore, container, false)

		arguments?.let {
			viewModel.passArguments(requireArguments().getString(RESTORE_FILE_PATH_KEY, ""))
		}

		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_restore_title)
		messageDisplay = view.findViewById(R.id.txt_restore_message)
		loadingProgressBar = view.findViewById(R.id.prog_restore)
		errorDisplay = view.findViewById(R.id.img_restore_fail)
		cancelButton = view.findViewById(R.id.btn_restore_cancel)
		cancelButton.setOnClickListener {
			dismiss()
		}
		confirmButton = view.findViewById(R.id.btn_restore_confirm)
		confirmButton.setOnClickListener {
			viewModel.restore()
		}
		dismissButton = view.findViewById(R.id.btn_restore_dismiss)
		dismissButton.setOnClickListener {
			dismiss()
		}
	}

	private fun setupObservers() {
		viewModel.displayButtons.observe(this, Observer { value ->
			value?.let { display ->
				cancelButton.show(display)
				confirmButton.show(display)
			}
		})

		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { display ->
				if(display) {
					loadingProgressBar.show(true)
					title.text = resources.getString(R.string.restoring)
				} else {
					loadingProgressBar.show(false)
				}
			}
		})

		viewModel.displayMessage.observe(this, Observer { value ->
			value?.let { display ->
				messageDisplay.show(display)
			}
		})

		viewModel.displayError.observe(this, Observer { value ->
			value?.let { display ->
				if(display) {
					errorDisplay.show(true)
					title.text = resources.getString(R.string.restore_error_title)
					messageDisplay.text = resources.getString(R.string.restore_error_message)
					messageDisplay.show(true)
				} else {
					errorDisplay.show(false)
				}
			}
		})

		viewModel.displayDismiss.observe(this, Observer { value ->
			value?.let { display ->
				dismissButton.show(display)
			}
		})

		viewModel.canDismiss.observe(this, Observer { value ->
			value?.let { canDismiss ->
				isCancelable = canDismiss
			}
		})
	}
}
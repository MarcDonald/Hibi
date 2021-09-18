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
package com.marcdonald.hibi.screens.settings.updatedialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show

class UpdateDialog : HibiDialogFragment() {

	private val viewModel by viewModels<UpdateDialogViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var noUpdateAvailable: ImageView
	private lateinit var errorDisplay: ImageView
	private lateinit var updateAvailable: ImageView
	private lateinit var noConnection: LinearLayout
	private lateinit var loadingProgressBar: ProgressBar
	private lateinit var openButton: MaterialButton
	private lateinit var dismissButton: MaterialButton
	private lateinit var title: TextView
	private lateinit var message: TextView
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_update, container, false)
		bindViews(view)
		setupObservers()
		viewModel.check()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_update_title)
		message = view.findViewById(R.id.txt_update_message)
		message.show(false)
		noConnection = view.findViewById(R.id.lin_update_no_connection)
		noUpdateAvailable = view.findViewById(R.id.img_no_update_available)
		updateAvailable = view.findViewById(R.id.img_update_available)
		updateAvailable.show(false)
		errorDisplay = view.findViewById(R.id.img_update_error)
		errorDisplay.show(false)
		loadingProgressBar = view.findViewById(R.id.prog_update)
		dismissButton = view.findViewById(R.id.btn_update_dismiss)
		dismissButton.setOnClickListener {
			dismiss()
		}
		openButton = view.findViewById(R.id.btn_update_open)
		openButton.setOnClickListener {
			val uriUrl = Uri.parse("https://github.com/MarcDonald/Hibi/releases/latest")
			val launchBrowser = Intent(Intent.ACTION_VIEW)
			launchBrowser.data = uriUrl
			startActivity(launchBrowser)

		}
	}

	private fun setupObservers() {
		viewModel.displayDismiss.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				dismissButton.show(display)
			}
		})

		viewModel.displayLoading.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				loadingProgressBar.show(display)
				if(display)
					title.text = resources.getString(R.string.checking_for_updates)
			}
		})

		viewModel.displayOpenButton.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				openButton.show(display)
			}
		})

		viewModel.displayNoUpdateAvailable.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				noUpdateAvailable.show(display)
				if(display) {
					title.text = resources.getString(R.string.no_update_title)
					message.text = resources.getString(R.string.no_update_message)
					message.show(true)
				}
			}
		})

		viewModel.displayNoConnection.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				noConnection.show(display)
				if(display) {
					title.text = resources.getString(R.string.no_connection_warning)
				}
			}
		})

		viewModel.displayRateLimitError.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				errorDisplay.show(display)
				if(display) {
					title.text = resources.getString(R.string.generic_error)
					message.text = resources.getString(R.string.github_rate_limit_exceeded)
					message.show(true)
				}
			}
		})


		viewModel.newVersionName.observe(viewLifecycleOwner, Observer { value ->
			value?.let { versionName ->
				title.text = resources.getString(R.string.new_version_available_title)
				message.text = resources.getString(R.string.new_version_available_message, versionName)
				message.show(true)
				updateAvailable.show(true)
			}
		})

		viewModel.displayError.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				errorDisplay.show(display)
				if(display)
					title.text = resources.getString(R.string.generic_error)
			}
		})
	}
}
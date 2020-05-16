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
package com.marcdonald.hibi.screens.settings.backupdialog

import android.content.Intent
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
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show

class BackupDialog : HibiDialogFragment() {

	private val viewModel by viewModels<BackupDialogViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingProgressBar: ProgressBar
	private lateinit var shareButton: MaterialButton
	private lateinit var successDisplay: ImageView
	private lateinit var dismissButton: MaterialButton
	private lateinit var title: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		isCancelable = false
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_backup, container, false)
		bindViews(view)
		setupObservers()
		viewModel.backup()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_backup_title)
		loadingProgressBar = view.findViewById(R.id.prog_backup)
		successDisplay = view.findViewById(R.id.img_backup_success)
		dismissButton = view.findViewById(R.id.btn_backup_dismiss)
		dismissButton.setOnClickListener {
			dismiss()
		}
		shareButton = view.findViewById(R.id.btn_backup_share)
		shareButton.setOnClickListener {
			shareOnClick()
		}
	}

	private fun setupObservers() {
		viewModel.displayDismiss.observe(this, Observer { value ->
			value?.let { dismissable ->
				dismissButton.show(dismissable)
				isCancelable = dismissable
			}
		})

		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { display ->
				loadingProgressBar.show(display)
			}
		})

		viewModel.displayShareButton.observe(this, Observer { value ->
			value?.let { display ->
				shareButton.show(display)
			}
		})

		viewModel.displaySuccess.observe(this, Observer { value ->
			value?.let { display ->
				if(display) {
					successDisplay.show(true)
					title.text = resources.getString(R.string.backup_success)
				} else {
					successDisplay.show(false)
					title.text = resources.getString(R.string.backing_up)
				}
			}
		})
	}

	private fun shareOnClick() {
		val uri = viewModel.localBackupURI
		uri?.let {
			val shareIntent = Intent(Intent.ACTION_SEND)
			shareIntent.type = "application/octet-stream"
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
			startActivity(shareIntent)
		}
		dismiss()
	}
}
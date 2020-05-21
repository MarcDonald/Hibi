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
package com.marcdonald.hibi.screens.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_BOOKS
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_LOCATION
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_TAGS
import com.marcdonald.hibi.internal.base.HibiDialogFragment

class MainEntriesDisplayPreferenceDialog : HibiDialogFragment() {
	// <editor-fold desc="UI Components">
	private lateinit var locationCheckBox: CheckBox
	private lateinit var tagsCheckBox: CheckBox
	private lateinit var booksCheckBox: CheckBox
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_main_entries_display_preference, container, false)
		bindViews(view)
		return view
	}

	private fun bindViews(view: View) {
		locationCheckBox = view.findViewById(R.id.chk_location)
		tagsCheckBox = view.findViewById(R.id.chk_tags)
		booksCheckBox = view.findViewById(R.id.chk_books)
		initCheckBoxes()
		view.findViewById<MaterialButton>(R.id.btn_checkbox_dialog_ok).setOnClickListener { onOkClick() }
	}

	private fun initCheckBoxes() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
		locationCheckBox.isChecked = prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_LOCATION, true)
		tagsCheckBox.isChecked = prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_TAGS, true)
		booksCheckBox.isChecked = prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_BOOKS, true)
	}

	private fun onOkClick() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
		prefs.edit().putBoolean(PREF_MAIN_ENTRY_DISPLAY_LOCATION, locationCheckBox.isChecked).apply()
		prefs.edit().putBoolean(PREF_MAIN_ENTRY_DISPLAY_TAGS, tagsCheckBox.isChecked).apply()
		prefs.edit().putBoolean(PREF_MAIN_ENTRY_DISPLAY_BOOKS, booksCheckBox.isChecked).apply()
		dismiss()
	}
}
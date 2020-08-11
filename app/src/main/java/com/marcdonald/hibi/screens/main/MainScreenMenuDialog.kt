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
package com.marcdonald.hibi.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment

class MainScreenMenuDialog : HibiBottomSheetDialogFragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_main_menu, container, false)
		bindViews(view)
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<MaterialButton>(R.id.btn_main_menu_statistics)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate(MainScreenFragmentDirections.statisticsAction())
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_favourite)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate( MainScreenFragmentDirections.favouritesAction())
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_throwback)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate(MainScreenFragmentDirections.throwbackAction())
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_new_words)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate(MainScreenFragmentDirections.newWordsAction())
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_settings)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate(MainScreenFragmentDirections.settingsAction())
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_about)
			.setOnClickListener {
				Navigation.findNavController(requireParentFragment().requireView()).navigate(MainScreenFragmentDirections.aboutAction())
				dismiss()
			}
	}
}
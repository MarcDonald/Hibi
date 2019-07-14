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
		view.findViewById<MaterialButton>(R.id.btn_main_menu_favourite)
			.setOnClickListener {
				val favouriteAction = MainScreenFragmentDirections.favouritesAction()
				Navigation.findNavController(requireParentFragment().requireView()).navigate(favouriteAction)
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_throwback)
			.setOnClickListener {
				val throwbackAction = MainScreenFragmentDirections.throwbackAction()
				Navigation.findNavController(requireParentFragment().requireView()).navigate(throwbackAction)
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_settings)
			.setOnClickListener {
				val settingsAction = MainScreenFragmentDirections.settingsAction()
				Navigation.findNavController(requireParentFragment().requireView()).navigate(settingsAction)
				dismiss()
			}

		view.findViewById<MaterialButton>(R.id.btn_main_menu_about)
			.setOnClickListener {
				val aboutAction = MainScreenFragmentDirections.aboutAction()
				Navigation.findNavController(requireParentFragment().requireView()).navigate(aboutAction)
				dismiss()
			}
	}
}
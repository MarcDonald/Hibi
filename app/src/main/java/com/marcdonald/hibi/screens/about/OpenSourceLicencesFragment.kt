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
package com.marcdonald.hibi.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.marcdonald.hibi.R
import com.marcdonald.simplelicensedisplay.SimpleLicenseDisplay

class OpenSourceLicencesFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_oss, container, false)
		bindViews(view)
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.open_source_licenses)
		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener(backClickListener)

		view.findViewById<SimpleLicenseDisplay>(R.id.license_timber)
			.setOnClickListener(openURLClickListener("https://github.com/JakeWharton/timber"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_kodein)
			.setOnClickListener(openURLClickListener("https://github.com/Kodein-Framework/Kodein-DI"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_retrofit)
			.setOnClickListener(openURLClickListener("https://github.com/square/retrofit"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_moshi)
			.setOnClickListener(openURLClickListener("https://github.com/square/moshi"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_android_file_picker)
			.setOnClickListener(openURLClickListener("https://github.com/DroidNinja/Android-FilePicker"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_android_image_picker)
			.setOnClickListener(openURLClickListener("https://github.com/esafirm/android-image-picker"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_mplus)
			.setOnClickListener(openURLClickListener("https://fonts.google.com/specimen/M+PLUS+Rounded+1c"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_open_sans)
			.setOnClickListener(openURLClickListener("https://fonts.google.com/specimen/Open+Sans"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_google_material_icons)
			.setOnClickListener(openURLClickListener("https://material.io/tools/icons/"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_material_icons)
			.setOnClickListener(openURLClickListener("https://materialdesignicons.com/"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_glide)
			.setOnClickListener(openURLClickListener("https://github.com/bumptech/glide"))

		view.findViewById<SimpleLicenseDisplay>(R.id.license_simple_license_display)
			.setOnClickListener(openURLClickListener("https://github.com/MarcDonald/SimpleLicenseDisplay"))
	}

	private val backClickListener = View.OnClickListener {
		Navigation.findNavController(requireView()).popBackStack()
	}

	private fun openURLClickListener(url: String): View.OnClickListener {
		return View.OnClickListener {
			val uriUrl = Uri.parse(url)
			val launchBrowser = Intent(Intent.ACTION_VIEW)
			launchBrowser.data = uriUrl
			startActivity(launchBrowser)
		}
	}
}
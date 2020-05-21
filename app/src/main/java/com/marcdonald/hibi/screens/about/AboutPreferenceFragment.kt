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
package com.marcdonald.hibi.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.marcdonald.hibi.BuildConfig
import com.marcdonald.hibi.R

class AboutPreferenceFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.about, rootKey)
		bindViews()
	}

	private fun bindViews() {
		initVersion()

		findPreference<Preference>("about_oss")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			val ossAction = AboutFragmentDirections.ossAction()
			Navigation.findNavController(requireView()).navigate(ossAction)

			true
		}

		findPreference<Preference>("about_jisho")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			launchURL("https://jisho.org/")
			true
		}

		findPreference<Preference>("about_privacy")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			val privacyAction = AboutFragmentDirections.privacyAction()
			Navigation.findNavController(requireView()).navigate(privacyAction)
			true
		}

		findPreference<Preference>("about_code")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			launchURL("https://github.com/MarcDonald/Hibi")
			true
		}

		findPreference<Preference>("about_author")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			launchURL("https://marcdonald.com")
			true
		}
	}

	private fun initVersion() {
		val version = findPreference<Preference>("about_version")
		version?.summary = BuildConfig.VERSION_NAME
		version?.setOnPreferenceClickListener {
			val buildCodeString = BuildConfig.VERSION_CODE.toString()
			Toast.makeText(requireContext(), resources.getString(R.string.build_code_with_output, buildCodeString), Toast.LENGTH_SHORT).show()
			true
		}
	}

	private fun launchURL(url: String) {
		val uriUrl = Uri.parse(url)
		val launchBrowser = Intent(Intent.ACTION_VIEW)
		launchBrowser.data = uriUrl
		startActivity(launchBrowser)
	}
}
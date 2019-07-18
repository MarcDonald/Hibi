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
package com.marcdonald.hibi.screens.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiFragment

class StatisticsFragment : HibiFragment() {

	private val viewModel by viewModels<StatisticsViewModel> { viewModelFactory }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_statistics, container, false)
		bindViews(view)
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
			Navigation.findNavController(view).popBackStack()
		}
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.statistics)
	}

	// TODO
}
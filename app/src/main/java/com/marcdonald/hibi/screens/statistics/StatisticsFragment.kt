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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.uicomponents.views.TextStatisticDisplay

class StatisticsFragment : HibiFragment() {

	private val viewModel by viewModels<StatisticsViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var totalEntriesDisplay: TextStatisticDisplay
	private lateinit var totalFavouritesDisplay: TextStatisticDisplay
	private lateinit var totalDaysDisplay: TextStatisticDisplay
	private lateinit var totalLocationsDisplay: TextStatisticDisplay
	private lateinit var totalTaggedEntriesDisplay: TextStatisticDisplay
	private lateinit var totalBookEntriesDisplay: TextStatisticDisplay
	private lateinit var totalNewWordsDisplay: TextStatisticDisplay
	private lateinit var mostNewWordsDisplay: TextStatisticDisplay
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_statistics, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
			Navigation.findNavController(view).popBackStack()
		}
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.statistics)
		totalEntriesDisplay = view.findViewById(R.id.stat_total_entries)
		totalDaysDisplay = view.findViewById(R.id.stat_total_days)
		totalFavouritesDisplay = view.findViewById(R.id.stat_total_favourites)    // TODO maybe click to go to favourites screen
		totalLocationsDisplay = view.findViewById(R.id.stat_total_locations)
		totalTaggedEntriesDisplay = view.findViewById(R.id.stat_total_tagged_entries)
		totalBookEntriesDisplay = view.findViewById(R.id.stat_total_entries_added_to_books)
		totalNewWordsDisplay = view.findViewById(R.id.stat_total_new_words)
		mostNewWordsDisplay = view.findViewById(R.id.stat_most_new_words_one_day)
	}

	private fun setupObservers() {
		viewModel.totalEntries.observe(this, Observer { value ->
			value?.let { totalEntries ->
				totalEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalEntries, totalEntries))
			}
		})

		viewModel.totalDays.observe(this, Observer { value ->
			value?.let { totalDays ->
				totalDaysDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_days, totalDays, totalDays))
			}
		})

		viewModel.totalFavourites.observe(this, Observer { value ->
			value?.let { totalFavourites ->
				totalFavouritesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalFavourites, totalFavourites))
			}
		})

		viewModel.totalLocations.observe(this, Observer { value ->
			value?.let { totalLocations ->
				totalLocationsDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_locations, totalLocations, totalLocations))
			}
		})

		viewModel.totalTaggedEntries.observe(this, Observer { value ->
			value?.let { totalTaggedEntries ->
				totalTaggedEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalTaggedEntries, totalTaggedEntries))
			}
		})

		viewModel.totalEntriesInBooks.observe(this, Observer { value ->
			value?.let { totalEntriesInBooks ->
				totalBookEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalEntriesInBooks, totalEntriesInBooks))
			}
		})

		viewModel.totalNewWords.observe(this, Observer { value ->
			value?.let { totalNewWords ->
				totalNewWordsDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_new_words, totalNewWords, totalNewWords))
			}
		})

		viewModel.mostNewWordsInOneDay.observe(this, Observer { value ->
			value?.let { mostNewWords ->
				mostNewWordsDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_new_words, mostNewWords, mostNewWords))
			}
		})
	}
}
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
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import com.marcdonald.hibi.uicomponents.views.TextStatisticDisplay
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class StatisticsFragment : HibiFragment(), KodeinAware {

	override val kodein by closestKodein()
	private val dateTimeUtils: DateTimeUtils by instance()

	private val viewModel by viewModels<StatisticsViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var totalEntriesDisplay: TextStatisticDisplay
	private lateinit var totalFavouritesDisplay: TextStatisticDisplay
	private lateinit var totalDaysDisplay: TextStatisticDisplay
	private lateinit var totalLocationsDisplay: TextStatisticDisplay
	private lateinit var totalTaggedEntriesDisplay: TextStatisticDisplay
	private lateinit var tagWithMostEntriesDisplay: TextStatisticDisplay
	private lateinit var totalBookEntriesDisplay: TextStatisticDisplay
	private lateinit var bookWithMostEntriesDisplay: TextStatisticDisplay
	private lateinit var totalNewWordsDisplay: TextStatisticDisplay
	private lateinit var mostNewWordsOneDayDisplay: TextStatisticDisplay
	private lateinit var mostNewWordsOneEntryDisplay: TextStatisticDisplay
	private lateinit var mostEntriesInOneDayDisplay: TextStatisticDisplay
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_statistics, container, false)
		bindViews(view)
		setupClickListeners(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
			Navigation.findNavController(view).popBackStack()
		}
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.statistics)

		// Some of these should open appropriate dates/entries when clicked
		totalEntriesDisplay = view.findViewById(R.id.stat_total_entries)
		totalDaysDisplay = view.findViewById(R.id.stat_total_days)
		totalFavouritesDisplay = view.findViewById(R.id.stat_total_favourites)
		totalLocationsDisplay = view.findViewById(R.id.stat_total_locations)
		totalTaggedEntriesDisplay = view.findViewById(R.id.stat_total_tagged_entries)
		tagWithMostEntriesDisplay = view.findViewById(R.id.stat_tag_with_most_entries)
		tagWithMostEntriesDisplay.show(false)
		totalBookEntriesDisplay = view.findViewById(R.id.stat_total_entries_added_to_books)
		bookWithMostEntriesDisplay = view.findViewById(R.id.stat_book_with_most_entries)
		bookWithMostEntriesDisplay.show(false)
		totalNewWordsDisplay = view.findViewById(R.id.stat_total_new_words)
		mostNewWordsOneDayDisplay = view.findViewById(R.id.stat_most_new_words_one_day)
		mostNewWordsOneDayDisplay.show(false)
		mostNewWordsOneEntryDisplay = view.findViewById(R.id.stat_most_new_words_one_entry)
		mostNewWordsOneEntryDisplay.show(false)
		mostEntriesInOneDayDisplay = view.findViewById(R.id.stat_most_entries_one_day)
		mostEntriesInOneDayDisplay.show(false)
	}

	private fun setupClickListeners(view: View) {
		mostNewWordsOneDayDisplay.setOnClickListener {
			viewModel.mostNewWordsInOneDay.value?.let { date ->
				// Defaults to 31/12/2 when nothing found, so this stops it being clickable in that case
				if(date.year > 2) {
					val action = StatisticsFragmentDirections.dateEntriesAction(date.day, date.month, date.year)
					Navigation.findNavController(view).navigate(action)
				}
			}
		}

		mostNewWordsOneEntryDisplay.setOnClickListener {
			viewModel.mostNewWordsInOneEntry.value?.let { entry ->
				if(entry.id > 0) {
					val action = StatisticsFragmentDirections.viewEntryAction()
					action.entryId = entry.id
					Navigation.findNavController(view).navigate(action)
				}
			}
		}

		mostEntriesInOneDayDisplay.setOnClickListener {
			viewModel.mostEntriesInOneDay.value?.let { date ->
				// Defaults to 31/12/2 when nothing found, so this stops it being clickable in that case
				if(date.year > 2) {
					val action = StatisticsFragmentDirections.dateEntriesAction(date.day, date.month, date.year)
					Navigation.findNavController(view).navigate(action)
				}
			}
		}

		totalFavouritesDisplay.setOnClickListener {
			val action = StatisticsFragmentDirections.favouritesAction()
			Navigation.findNavController(view).navigate(action)
		}

		tagWithMostEntriesDisplay.setOnClickListener {
			viewModel.tagWithMostEntries.value?.let { tag ->
				val action = StatisticsFragmentDirections.viewTaggedEntriesAction(tag.id)
				Navigation.findNavController(view).navigate(action)
			}
		}

		bookWithMostEntriesDisplay.setOnClickListener {
			viewModel.bookWithMostEntries.value?.let { book ->
				val action = StatisticsFragmentDirections.viewBookEntriesAction(book.id)
				Navigation.findNavController(view).navigate(action)
			}
		}
	}

	private fun setupObservers() {
		viewModel.totalEntries.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalEntries ->
				totalEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalEntries, totalEntries))
			}
		})

		viewModel.totalDays.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalDays ->
				totalDaysDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_days, totalDays, totalDays))
			}
		})

		viewModel.totalFavourites.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalFavourites ->
				totalFavouritesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalFavourites, totalFavourites))
			}
		})

		viewModel.totalLocations.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalLocations ->
				totalLocationsDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_locations, totalLocations, totalLocations))
			}
		})

		viewModel.totalTaggedEntries.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalTaggedEntries ->
				totalTaggedEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalTaggedEntries, totalTaggedEntries))
			}
		})

		viewModel.tagWithMostEntries.observe(viewLifecycleOwner, Observer { value ->
			value?.let { tag ->
				tagWithMostEntriesDisplay.show(true)
				tagWithMostEntriesDisplay.setMessage(tag.name)
			}
		})

		viewModel.totalEntriesInBooks.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalEntriesInBooks ->
				totalBookEntriesDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, totalEntriesInBooks, totalEntriesInBooks))
			}
		})

		viewModel.bookWithMostEntries.observe(viewLifecycleOwner, Observer { value ->
			value?.let { book ->
				bookWithMostEntriesDisplay.show(true)
				bookWithMostEntriesDisplay.setMessage(book.name)
			}
		})

		viewModel.totalNewWords.observe(viewLifecycleOwner, Observer { value ->
			value?.let { totalNewWords ->
				totalNewWordsDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_new_words, totalNewWords, totalNewWords))
			}
		})

		viewModel.mostNewWordsInOneDay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { newValue ->
				val mostNewWords = newValue.number
				mostNewWordsOneDayDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_new_words, mostNewWords, mostNewWords))
				if(newValue.number > 0) {
					mostNewWordsOneDayDisplay.show(true)
					val date = dateTimeUtils.formatDateForDisplay(newValue.day, newValue.month, newValue.year)
					mostNewWordsOneDayDisplay.setSecondaryMessage(resources.getString(R.string.on_date, date))
				} else {
					mostNewWordsOneDayDisplay.show(false)
					mostNewWordsOneDayDisplay.showSecondaryMessage(false)
				}
			}
		})

		viewModel.mostNewWordsInOneEntry.observe(viewLifecycleOwner, Observer { value ->
			value?.let { newValue ->
				val mostNewWords = newValue.number
				if(newValue.number > 0) {
					mostNewWordsOneEntryDisplay.show(true)
					mostNewWordsOneEntryDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_new_words, mostNewWords, mostNewWords))
				} else {
					mostNewWordsOneEntryDisplay.show(false)
				}
			}
		})

		viewModel.mostEntriesInOneDay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { newValue ->
				val mostEntries = newValue.number
				mostEntriesInOneDayDisplay.setMessage(resources.getQuantityString(R.plurals.stat_total_entries, mostEntries, mostEntries))
				if(newValue.number > 1) {
					mostEntriesInOneDayDisplay.show(true)
					val date = dateTimeUtils.formatDateForDisplay(newValue.day, newValue.month, newValue.year)
					mostEntriesInOneDayDisplay.setSecondaryMessage(resources.getString(R.string.on_date, date))
				} else {
					mostEntriesInOneDayDisplay.show(false)
				}
			}
		})
	}
}
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
package com.marcdonald.hibi.screens.calendar

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.main.MainScreenFragmentDirections
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter

class CalendarFragment : HibiFragment() {

	private val viewModel by viewModels<CalendarTabViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var calendarView: CalendarView
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_calendar, container, false)

		bindViews(view)
		initRecycler(view)
		setupObservers()
		viewModel.loadData()

		return view
	}

	private fun bindViews(view: View) {
		calendarView = view.findViewById(R.id.calendar_calendarview)
		calendarView.setOnDateChangeListener(calendarViewDateChangeListener)

		loadingDisplay = view.findViewById(R.id.const_calendar_entries_loading)
		loadingDisplay.show(false)

		noResults = view.findViewById(R.id.const_no_calendar_entries)
		noResults.show(false)
	}

	private val calendarViewDateChangeListener = CalendarView.OnDateChangeListener { _, year, month, day ->
		viewModel.loadEntriesForDate(year, month, day)
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.calendar_entries)
		this.recyclerAdapter = EntriesRecyclerAdapter(requireContext(), ::onEntryClick, requireActivity().theme)
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
		if(includeEntryDividers) {
			val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
			recycler.addItemDecoration(dividerItemDecoration)
		}
	}

	private fun setupObservers() {
		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { shouldShow ->
				loadingDisplay.show(shouldShow)
			}
		})

		viewModel.displayNoResults.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noResults.show(shouldShow)
			}
		})

		viewModel.entries.observe(this, Observer { items ->
			items?.let {
				recyclerAdapter.updateList(items)
			}
		})
	}

	private fun onEntryClick(entryId: Int) {
		val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
		viewEntryAction.entryId = entryId
		Navigation.findNavController(requireView()).navigate(viewEntryAction)
	}
}
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
package com.marcdonald.hibi.screens.tags.taggedentries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_DATE_HEADER_PERIOD
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesHeaderItemDecoration
import timber.log.Timber

class TaggedEntriesFragment : HibiFragment() {

	private val viewModel by viewModels<TaggedEntriesViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	private lateinit var toolbarTitle: TextView
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	// </editor-fold>=

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_tagged_entries, container, false)

		bindViews(view)
		initRecycler(view)
		setupObservers()

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { arguments ->
			Timber.i("Log: onViewCreated: ${TaggedEntriesFragmentArgs.fromBundle(arguments).tagID}")
			viewModel.passArguments(TaggedEntriesFragmentArgs.fromBundle(arguments).tagID)
		}
		viewModel.loadEntries()
	}

	private fun bindViews(view: View) {
		loadingDisplay = view.findViewById(R.id.const_tagged_entries_loading)
		noResults = view.findViewById(R.id.const_no_tagged_entries)
		toolbarTitle = view.findViewById(R.id.txt_back_toolbar_title)
		val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
		toolbarBack.setOnClickListener {
			Navigation.findNavController(requireView()).popBackStack()
		}
	}

	private fun setupObservers() {
		viewModel.toolbarTitle.observe(viewLifecycleOwner, Observer { value ->
			value?.let { title ->
				toolbarTitle.text = title
			}
		})

		viewModel.entries.observe(viewLifecycleOwner, Observer { value ->
			value?.let { list ->
				recyclerAdapter.updateList(list)
			}
		})

		viewModel.displayLoading.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				loadingDisplay.show(shouldShow)
			}
		})

		viewModel.displayNoResults.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				noResults.show(shouldShow)
			}
		})
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_tagged_entries)
		this.recyclerAdapter = EntriesRecyclerAdapter(requireContext(), ::onEntryClick, requireActivity().theme)
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
		if(prefs.getBoolean(PREF_ENTRY_DIVIDERS, true)) {
			val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
			recycler.addItemDecoration(dividerItemDecoration)
		}
		if(prefs.getString(PREF_DATE_HEADER_PERIOD, "1") != "0") {
			val decoration = MainEntriesHeaderItemDecoration(recycler, recyclerAdapter)
			recycler.addItemDecoration(decoration)
		}
	}

	private fun onEntryClick(entryId: Int) {
		val viewEntryAction = TaggedEntriesFragmentDirections.viewEntryAction()
		viewEntryAction.entryId = entryId
		Navigation.findNavController(requireView()).navigate(viewEntryAction)
	}
}
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
package com.marcdonald.hibi.screens.booksscreen.bookentriesfragment

import android.os.Bundle
import android.preference.PreferenceManager
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
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesHeaderItemDecoration

class BookEntriesFragment : HibiFragment() {

	private val viewModel by viewModels<BookEntriesViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	private lateinit var toolbarTitle: TextView
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_book_entries, container, false)

		bindViews(view)
		initRecycler(view)
		setupObservers()
		viewModel.loadEntries()
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { arguments ->
			viewModel.passArguments(BookEntriesFragmentArgs.fromBundle(arguments).bookID)
		}
	}

	private fun bindViews(view: View) {
		loadingDisplay = view.findViewById(R.id.const_book_entries_loading)
		noResults = view.findViewById(R.id.const_no_book_entries)
		toolbarTitle = view.findViewById(R.id.txt_back_toolbar_title)
		val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
		toolbarBack.setOnClickListener {
			Navigation.findNavController(requireView()).popBackStack()
		}
	}

	private fun setupObservers() {
		viewModel.toolbarTitle.observe(this, Observer { value ->
			value?.let { title ->
				toolbarTitle.text = title
			}
		})

		viewModel.entries.observe(this, Observer { value ->
			value?.let { list ->
				recyclerAdapter.updateList(list)
			}
		})

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
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_book_entries)
		this.recyclerAdapter = EntriesRecyclerAdapter(requireContext(), requireActivity().theme)
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
		if(includeEntryDividers) {
			val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
			recycler.addItemDecoration(dividerItemDecoration)
		}

		val decoration = MainEntriesHeaderItemDecoration(recycler, recyclerAdapter)
		recycler.addItemDecoration(decoration)
	}
}
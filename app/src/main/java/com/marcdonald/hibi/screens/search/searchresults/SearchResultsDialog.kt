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
package com.marcdonald.hibi.screens.search.searchresults

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.internal.SEARCH_TERM_KEY
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.extension.show
import timber.log.Timber

class SearchResultsDialog : HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var progressBar: ProgressBar
	private lateinit var noConnectionWarning: LinearLayout
	private lateinit var noResultsWarning: LinearLayout
	private lateinit var recyclerAdapter: SearchResultsRecyclerAdapter
	private lateinit var recycler: RecyclerView
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Timber.v("Log: onCreateView: Started")
		val view = inflater.inflate(R.layout.dialog_search, container, false)
		bindViews(view)
		initRecycler()
		setupObservers()
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { arguments ->
			val searchTerm = arguments.getString(SEARCH_TERM_KEY, "")
			viewModel.entryId = arguments.getInt(ENTRY_ID_KEY, 0)
			val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(requireView().windowToken, 0)
			viewModel.search(searchTerm)
		}
	}

	private fun bindViews(view: View) {
		recycler = view.findViewById(R.id.recycler_search_results)
		recycler.show(false)

		progressBar = view.findViewById(R.id.prog_search_results)
		progressBar.show(false)

		noConnectionWarning = view.findViewById(R.id.lin_search_no_connection)
		noConnectionWarning.show(false)

		noResultsWarning = view.findViewById(R.id.lin_search_no_results)
		noResultsWarning.show(false)
	}

	private fun initRecycler() {
		this.recyclerAdapter = SearchResultsRecyclerAdapter(requireContext(), requireFragmentManager())
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
		recycler.addItemDecoration(dividerItemDecoration)
	}

	private fun setupObservers() {
		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { shouldShow ->
				progressBar.show(shouldShow)
			}
		})

		viewModel.displayNoConnection.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noConnectionWarning.show(shouldShow)
			}
		})

		viewModel.displayNoResults.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noResultsWarning.show(shouldShow)
			}
		})

		viewModel.searchResults.observe(this, Observer { value ->
			value?.let { searchResult ->
				recyclerAdapter.updateList(searchResult, viewModel.entryId)
				recycler.scrollToPosition(0)
				recycler.show(true)
			}
		})
	}
}
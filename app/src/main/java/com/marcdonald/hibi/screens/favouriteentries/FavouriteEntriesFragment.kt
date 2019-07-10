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
package com.marcdonald.hibi.screens.favouriteentries

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesHeaderItemDecoration
import com.marcdonald.hibi.uicomponents.BinaryOptionDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavouriteEntriesFragment : Fragment(), KodeinAware {
	override val kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: FavouriteEntriesViewModelFactory by instance()
	private lateinit var viewModel: FavouriteEntriesViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noEntries: ConstraintLayout
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteEntriesViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_favourite_entries, container, false)

		bindViews(view)
		initRecycler(view)
		setupObservers()
		viewModel.loadEntries()

		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.favourites)
		loadingDisplay = view.findViewById(R.id.const_favourite_entries_loading)
		noEntries = view.findViewById(R.id.const_no_favourites)
		noEntries.show(false)
		val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
		toolbarBack.setOnClickListener {
			Navigation.findNavController(requireView()).popBackStack()
		}
	}

	private fun setupObservers() {
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
				noEntries.show(shouldShow)
			}
		})
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_favourite_entries)
		this.recyclerAdapter = EntriesRecyclerAdapter(requireContext(), true, onSelectClick, requireActivity().theme)
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

	private val onSelectClick = View.OnClickListener {
		val removeDialog = BinaryOptionDialog()
		val selectedAmount = recyclerAdapter.getSelectedEntryIds().size
		removeDialog.setTitle(resources.getQuantityString(R.plurals.multi_favourites_remove_title, selectedAmount, selectedAmount))
		removeDialog.setMessage(resources.getQuantityString(R.plurals.multi_favourites_remove_message, selectedAmount, selectedAmount))
		removeDialog.setNegativeButton(resources.getString(R.string.remove), View.OnClickListener {
			viewModel.removeSelectedItemsFromFavourites(recyclerAdapter.getSelectedEntryIds())
			removeDialog.dismiss()
		})
		removeDialog.setPositiveButton(resources.getString(R.string.cancel), View.OnClickListener { removeDialog.dismiss() })
		removeDialog.show(requireFragmentManager(), "Confirm Multi Favourite Remove Dialog")
	}
}
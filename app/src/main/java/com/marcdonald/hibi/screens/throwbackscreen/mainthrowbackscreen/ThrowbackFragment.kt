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
package com.marcdonald.hibi.screens.throwbackscreen.mainthrowbackscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show

class ThrowbackFragment : HibiFragment() {

	private val viewModel by viewModels<ThrowbackFragmentViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noEntriesDisplay: ConstraintLayout
	private lateinit var recyclerAdapter: ThrowbackRecyclerAdapter
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_throwback, container, false)
		bindViews(view)
		initRecycler(view)
		setupObservers()
		viewModel.loadEntries()
		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.throwback)
		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
			Navigation.findNavController(view).popBackStack()
		}
		loadingDisplay = view.findViewById(R.id.const_throwback_loading)
		loadingDisplay.show(false)
		noEntriesDisplay = view.findViewById(R.id.const_no_throwback)
		noEntriesDisplay.show(false)
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_throwback)
		this.recyclerAdapter = ThrowbackRecyclerAdapter(requireContext(), ::onItemClick)
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager
	}

	private fun setupObservers() {
		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { shouldShow ->
				loadingDisplay.show(shouldShow)
			}
		})

		viewModel.displayNoResults.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noEntriesDisplay.show(shouldShow)
			}
		})

		viewModel.displayItems.observe(this, Observer { items ->
			items?.let {
				recyclerAdapter.updateList(items)
			}
		})
	}

	private fun onItemClick(amountOfOtherEntries: Int, entryId: Int, day: Int, month: Int, year: Int) {
		val action: NavDirections
		if(amountOfOtherEntries == 0) {
			action = ThrowbackFragmentDirections.viewEntryAction()
			action.entryId = entryId
		} else {
			action = ThrowbackFragmentDirections.throwbackEntriesAction(day, month, year)
		}
		Navigation.findNavController(requireParentFragment().requireView()).navigate(action)
	}
}
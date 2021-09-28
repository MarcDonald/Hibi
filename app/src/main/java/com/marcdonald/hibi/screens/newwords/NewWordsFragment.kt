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
package com.marcdonald.hibi.screens.newwords

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show

class NewWordsFragment : HibiFragment() {

	private val viewModel by viewModels<NewWordsViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noNewWords: ConstraintLayout
	private lateinit var recyclerAdapter: NewWordsRecyclerAdapter
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_new_words, container, false)

		bindViews(view)
		initRecycler(view)
		setupObservers()

		return view
	}

	private fun bindViews(view: View) {
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.new_words)
		loadingDisplay = view.findViewById(R.id.const_new_words_loading)
		noNewWords = view.findViewById(R.id.const_no_new_words)
		noNewWords.show(false)
		val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
		toolbarBack.setOnClickListener {
			Navigation.findNavController(requireView()).popBackStack()
		}
	}

	private fun setupObservers() {
		viewModel.newWords.observe(viewLifecycleOwner, Observer { value ->
			if(value != null) {
				loadingDisplay.show(false)
			}
			value?.let { list ->
				noNewWords.show(list.isEmpty())
				recyclerAdapter.updateList(list)
			}
		})
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_new_words)
		this.recyclerAdapter = NewWordsRecyclerAdapter(requireContext(), ::onWordClick)
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager
	}

	private fun onWordClick(entryId: Int) {
		val viewEntryAction = NewWordsFragmentDirections.viewEntryAction()
		viewEntryAction.entryId = entryId
		Navigation.findNavController(requireView()).navigate(viewEntryAction)
	}
}
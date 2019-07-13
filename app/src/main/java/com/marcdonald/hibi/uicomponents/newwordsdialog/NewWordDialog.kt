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
package com.marcdonald.hibi.uicomponents.newwordsdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.internal.IS_EDIT_MODE_KEY
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.uicomponents.addnewworddialog.AddNewWordDialog

class NewWordDialog : HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<NewWordViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var noResultsWarning: LinearLayout
	private lateinit var addButton: MaterialButton
	private lateinit var recyclerAdapter: NewWordsRecyclerAdapter
	private lateinit var recycler: RecyclerView
	// </editor-fold>

	// <editor-fold desc="Other">
	private var isEditMode = true
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_new_word, container, false)
		arguments?.let { arguments ->
			viewModel.passArguments(arguments.getInt(ENTRY_ID_KEY, 0), arguments.getBoolean(IS_EDIT_MODE_KEY, true))
		}
		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		noResultsWarning = view.findViewById(R.id.lin_new_words_no_results)
		recycler = view.findViewById(R.id.recycler_new_words)

		addButton = view.findViewById(R.id.btn_add_new_word)
		addButton.setOnClickListener(addClickListener)

		initRecycler()
	}

	private fun setupObservers() {
		viewModel.displayAddButton.observe(this, Observer { value ->
			value?.let { shouldShow ->
				addButton.show(shouldShow)
			}
		})

		viewModel.allowEdits.observe(this, Observer { value ->
			value?.let { allow ->
				recyclerAdapter.isEditMode = allow
			}
		})

		viewModel.displayNoWords.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noResultsWarning.show(shouldShow)
			}
		})

		viewModel.getNewWords().observe(this, Observer { value ->
			value?.let { list ->
				viewModel.listReceived(list.isEmpty())
				recyclerAdapter.updateList(list)
			}
		})
	}

	private val addClickListener = View.OnClickListener {
		val dialog = AddNewWordDialog()

		val bundle = Bundle()
		bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
		dialog.arguments = bundle

		dialog.show(requireFragmentManager(), "Add New Word Dialog")
	}

	private fun initRecycler() {
		this.recyclerAdapter = NewWordsRecyclerAdapter(requireContext(), requireFragmentManager())
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
		recycler.addItemDecoration(dividerItemDecoration)
	}
}
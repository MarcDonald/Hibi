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
package com.marcdonald.hibi.screens.mainentries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesHeaderItemDecoration
import com.marcdonald.hibi.uicomponents.BinaryOptionDialog
import com.marcdonald.hibi.uicomponents.TextInputDialog
import com.marcdonald.hibi.uicomponents.multiselectdialog.MultiSelectMenu
import com.marcdonald.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog.AddMultiEntryToBookDialog
import com.marcdonald.hibi.uicomponents.multiselectdialog.addtagtomultientrydialog.AddTagToMultiEntryDialog

class MainEntriesFragment : HibiFragment() {

	private val viewModel by viewModels<MainEntriesViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	// </editor-fold>

	private val backPressCallback = object : OnBackPressedCallback(false) {
		override fun handleOnBackPressed() {
			if(recyclerAdapter.getSelectedEntryIds().isNotEmpty())
				recyclerAdapter.clearSelectedEntries()
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_main_entries, container, false)
		bindViews(view)
		initRecycler(view)
		setupObservers()
		requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
		viewModel.loadEntries()
		return view
	}

	private fun bindViews(view: View) {
		loadingDisplay = view.findViewById(R.id.const_entries_loading)
		loadingDisplay.show(false)

		noResults = view.findViewById(R.id.const_no_entries)
		noResults.show(false)
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_entries)
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

		recyclerAdapter.hasSelectedItems.observe(this, Observer { value ->
			value?.let { hasSelectedItems ->
				backPressCallback.isEnabled = hasSelectedItems
			}
		})
	}

	private val onSelectClick = View.OnClickListener {
		val menu = MultiSelectMenu(onMultiSelectMenuItemSelected)
		menu.show(requireFragmentManager(), "Select Menu")
	}

	private val onMultiSelectMenuItemSelected = object : MultiSelectMenu.ItemSelectedListener {
		override fun itemSelected(item: Int) {
			when(item) {
				MultiSelectMenu.TAG       -> initMultiTagDialog()
				MultiSelectMenu.BOOK      -> initMultiBookDialog()
				MultiSelectMenu.LOCATION  -> initMultiLocationSetDialog()
				MultiSelectMenu.DELETE    -> initMultiDeleteDialog()
				MultiSelectMenu.FAVOURITE -> initMultiFavouriteDialog()
			}
		}
	}

	private fun initMultiTagDialog() {
		val dialog = AddTagToMultiEntryDialog(recyclerAdapter.getSelectedEntryIds().size) { deleteMode: Boolean, tagIds: List<Int> ->
			viewModel.setTagsOfSelectedEntries(deleteMode, tagIds, recyclerAdapter.getSelectedEntryIds())
		}
		dialog.show(requireFragmentManager(), "Set Multi Entry Tags")
	}

	private fun initMultiBookDialog() {
		val dialog = AddMultiEntryToBookDialog(recyclerAdapter.getSelectedEntryIds().size) { deleteMode: Boolean, bookIds: List<Int> ->
			viewModel.setBooksOfSelectedEntries(deleteMode, bookIds, recyclerAdapter.getSelectedEntryIds())
		}
		dialog.show(requireFragmentManager(), "Set Multi Entry Books")
	}

	private fun initMultiLocationSetDialog() {
		val locationDialog = TextInputDialog()
		val selectedAmount = recyclerAdapter.getSelectedEntryIds().size
		locationDialog.setTitle(resources.getQuantityString(R.plurals.multi_location_title, selectedAmount, selectedAmount))
		locationDialog.setHint(resources.getString(R.string.location))
		locationDialog.setDeleteClickListener(View.OnClickListener {
			viewModel.addLocationToSelectedEntries("", recyclerAdapter.getSelectedEntryIds())
			locationDialog.dismiss()
		})
		locationDialog.setSaveClickListener(object : TextInputDialog.TextInputDialogSaveListener {
			override fun onSave(text: String) {
				viewModel.addLocationToSelectedEntries(text, recyclerAdapter.getSelectedEntryIds())
				locationDialog.dismiss()
			}
		})
		locationDialog.show(requireFragmentManager(), "Set Multi Location Dialog")
	}

	private fun initMultiDeleteDialog() {
		val deleteConfirmDialog = BinaryOptionDialog()
		val selectedAmount = recyclerAdapter.getSelectedEntryIds().size
		deleteConfirmDialog.setTitle(resources.getQuantityString(R.plurals.multi_delete_title, selectedAmount, selectedAmount))
		deleteConfirmDialog.setMessage(resources.getQuantityString(R.plurals.multi_delete_message, selectedAmount, selectedAmount))
		deleteConfirmDialog.setNegativeButton(resources.getString(R.string.delete), View.OnClickListener {
			viewModel.deleteSelectedEntries(recyclerAdapter.getSelectedEntryIds())
			deleteConfirmDialog.dismiss()
		})
		deleteConfirmDialog.setPositiveButton(resources.getString(R.string.cancel), View.OnClickListener { deleteConfirmDialog.dismiss() })
		deleteConfirmDialog.show(requireFragmentManager(), "Confirm Multi Delete Dialog")
	}

	private fun initMultiFavouriteDialog() {
		val addOrRemoveFavouriteDialog = BinaryOptionDialog()
		val selectedAmount = recyclerAdapter.getSelectedEntryIds().size
		addOrRemoveFavouriteDialog.setTitle(resources.getQuantityString(R.plurals.multi_favourite_title, selectedAmount, selectedAmount))
		addOrRemoveFavouriteDialog.setMessage(resources.getQuantityString(R.plurals.multi_favourite_message, selectedAmount, selectedAmount))
		addOrRemoveFavouriteDialog.setNegativeButton(resources.getString(R.string.remove), View.OnClickListener {
			viewModel.setSelectedEntriesFavourited(false, recyclerAdapter.getSelectedEntryIds())
			addOrRemoveFavouriteDialog.dismiss()
		})
		addOrRemoveFavouriteDialog.setPositiveButton(resources.getString(R.string.add), View.OnClickListener {
			viewModel.setSelectedEntriesFavourited(true, recyclerAdapter.getSelectedEntryIds())
			addOrRemoveFavouriteDialog.dismiss()
		})
		addOrRemoveFavouriteDialog.show(requireFragmentManager(), "Multi Favourite Dialog")
	}
}
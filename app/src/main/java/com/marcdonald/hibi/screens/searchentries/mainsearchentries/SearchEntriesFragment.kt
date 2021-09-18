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
package com.marcdonald.hibi.screens.searchentries.mainsearchentries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_DATE_HEADER_PERIOD
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.mainentriesrecycler.EntriesRecyclerAdapter
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesHeaderItemDecoration
import com.marcdonald.hibi.uicomponents.DatePickerDialog
import com.marcdonald.hibi.uicomponents.TextInputDialog
import com.marcdonald.hibi.uicomponents.views.ChipWithId

class SearchEntriesFragment : HibiFragment() {

	private val viewModel by viewModels<SearchEntriesViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	private lateinit var toolbarTitle: TextView
	private lateinit var recycler: RecyclerView
	private lateinit var recyclerAdapter: EntriesRecyclerAdapter
	private lateinit var criteriaBottomSheetRelativeLayout: RelativeLayout
	private lateinit var criteriaBottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
	// </editor-fold>

	// <editor-fold desc="Bottom Sheet UI Components">
	private lateinit var beginningDateButton: MaterialButton
	private lateinit var endDateButton: MaterialButton
	private lateinit var containingButton: MaterialButton
	private lateinit var locationButton: MaterialButton
	private lateinit var tagChipGroup: ChipGroup
	private lateinit var noTagsWarning: TextView
	private lateinit var bookChipGroup: ChipGroup
	private lateinit var noBooksWarning: TextView
	private lateinit var searchButton: MaterialButton
	private lateinit var resetButton: MaterialButton
	private lateinit var startDateDialog: DatePickerDialog
	private lateinit var endDateDialog: DatePickerDialog
	private lateinit var containingDialog: TextInputDialog
	private lateinit var locationDialog: TextInputDialog
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_search_entries, container, false)

		bindViews(view)
		initRecycler(view)
		setupMainObservers()
		setupBottomSheetObservers()

		val backPressCallback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if(criteriaBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
					criteriaBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
				else
					Navigation.findNavController(requireView()).popBackStack()
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)

		return view
	}

	private fun bindViews(view: View) {
		criteriaBottomSheetRelativeLayout = view.findViewById(R.id.bottom_sheet_search_entries)
		criteriaBottomSheetBehavior = BottomSheetBehavior.from(criteriaBottomSheetRelativeLayout)
		loadingDisplay = view.findViewById(R.id.const_search_entries_loading)
		noResults = view.findViewById(R.id.const_no_search_entries_results)
		toolbarTitle = view.findViewById(R.id.txt_back_toolbar_title)
		toolbarTitle.text = resources.getString(R.string.search_entries)
		val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
		toolbarBack.setOnClickListener {
			Navigation.findNavController(requireView()).popBackStack()
		}
		initBottomSheet(view)
	}

	private fun setupMainObservers() {
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

		viewModel.countResults.observe(viewLifecycleOwner, Observer { value ->
			value?.let { amount ->
				toolbarTitle.text = resources.getQuantityString(R.plurals.count_results, amount, amount)
			}
		})

		viewModel.displayNoResults.observe(viewLifecycleOwner, Observer { value ->
			value?.let { show ->
				if(show) {
					noResults.show(true)
					recycler.show(false)
				} else {
					noResults.show(false)
					recycler.show(true)
				}
			}
		})
	}

	private fun initRecycler(view: View) {
		recycler = view.findViewById(R.id.recycler_search_entries)
		recyclerAdapter = EntriesRecyclerAdapter(requireContext(), ::onEntryClick, requireActivity().theme)
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

	private fun initBottomSheet(view: View) {
		tagChipGroup = view.findViewById(R.id.cg_search_entries_tags)
		noTagsWarning = view.findViewById(R.id.txt_search_entries_no_tags)
		bookChipGroup = view.findViewById(R.id.cg_search_entries_books)
		noBooksWarning = view.findViewById(R.id.txt_search_entries_no_books)

		beginningDateButton = view.findViewById(R.id.btn_search_entries_beginning)
		beginningDateButton.setOnClickListener {
			initStartDateDialog()
			startDateDialog.show(requireFragmentManager(), "Start Date Dialog")
		}
		beginningDateButton.setOnLongClickListener {
			viewModel.resetStartDate()
			true
		}

		endDateButton = view.findViewById(R.id.btn_search_entries_end)
		endDateButton.setOnClickListener {
			initEndDateDialog()
			endDateDialog.show(requireFragmentManager(), "End Date Dialog")
		}
		endDateButton.setOnLongClickListener {
			viewModel.resetEndDate()
			true
		}

		searchButton = view.findViewById(R.id.btn_search_entries_go)
		searchButton.setOnClickListener {
			viewModel.search(
				getCheckedTags(),
				getCheckedBooks()
			)
		}

		containingButton = view.findViewById(R.id.btn_containing)
		containingButton.setOnClickListener {
			initContainingDialog()
			containingDialog.show(requireFragmentManager(), "Containing Input Dialog")
		}
		containingButton.setOnLongClickListener {
			viewModel.resetContaining()
			true
		}

		locationButton = view.findViewById(R.id.btn_location)
		locationButton.setOnClickListener {
			initLocationDialog()
			locationDialog.show(requireFragmentManager(), "Location Input Dialog")
		}
		locationButton.setOnLongClickListener {
			viewModel.resetLocation()
			true
		}

		val dragHandle: ImageView = view.findViewById(R.id.img_search_entries_drag_handle)
		dragHandle.setOnClickListener {
			if(criteriaBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
				criteriaBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			else
				criteriaBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
		}

		resetButton = view.findViewById(R.id.btn_search_entries_reset)
		resetButton.setOnClickListener { viewModel.reset() }
	}

	private fun initStartDateDialog() {
		val builder = DatePickerDialog.Builder()
		builder
			.setOkClickListener(View.OnClickListener {
				viewModel.setStartDate(startDateDialog.year, startDateDialog.month, startDateDialog.day)
				startDateDialog.dismiss()
			})
			.setCancelClickListener(View.OnClickListener {
				startDateDialog.dismiss()
			})
			.showExtraButton(resources.getString(R.string.start), View.OnClickListener {
				viewModel.resetStartDate()
				startDateDialog.dismiss()
			})
		if(!viewModel.startIsBeginning())
			builder.initDatePicker(viewModel.startYear, viewModel.startMonth, viewModel.startDay, null)

		startDateDialog = builder.build()
	}

	private fun initEndDateDialog() {
		val builder = DatePickerDialog.Builder()
		builder
			.setOkClickListener(View.OnClickListener {
				viewModel.setEndDate(endDateDialog.year, endDateDialog.month, endDateDialog.day)
				endDateDialog.dismiss()
			})
			.setCancelClickListener(View.OnClickListener {
				endDateDialog.dismiss()
			})
			.showExtraButton(resources.getString(R.string.finish), View.OnClickListener {
				viewModel.resetEndDate()
				endDateDialog.dismiss()
			})
		if(!viewModel.endIsFinish())
			builder.initDatePicker(viewModel.endYear, viewModel.endMonth, viewModel.endDay, null)

		endDateDialog = builder.build()
	}

	private fun initContainingDialog() {
		val builder = TextInputDialog.Builder()
		builder
			.setSaveClickListener(containingSaveListener)
			.setDeleteClickListener(View.OnClickListener {
				viewModel.resetContaining()
				containingDialog.dismiss()
			})
			.setHint(resources.getString(R.string.content))
			.setTitle(resources.getString(R.string.content))
		viewModel.containingDisplay.value?.let { currentContent ->
			builder.setContent(currentContent)
		}
		containingDialog = builder.build()
	}

	private fun initLocationDialog() {
		val builder = TextInputDialog.Builder()
		builder
			.setSaveClickListener(locationSaveListener)
			.setDeleteClickListener(View.OnClickListener {
				viewModel.resetLocation()
				locationDialog.dismiss()
			})
			.setHint(resources.getString(R.string.location))
			.setTitle(resources.getString(R.string.location))
		viewModel.locationDisplay.value?.let { currentLocation ->
			builder.setContent(currentLocation)
		}
		locationDialog = builder.build()
	}

	private val containingSaveListener = object : TextInputDialog.TextInputDialogSaveListener {
		override fun onSave(text: String) {
			viewModel.setContaining(text)
			containingDialog.dismiss()
		}
	}

	private val locationSaveListener = object : TextInputDialog.TextInputDialogSaveListener {
		override fun onSave(text: String) {
			viewModel.setLocation(text)
			locationDialog.dismiss()
		}
	}

	private fun setupBottomSheetObservers() {
		viewModel.beginningDisplay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { beginningText ->
				if(beginningText.isBlank())
					beginningDateButton.text = resources.getString(R.string.start)
				else
					beginningDateButton.text = beginningText
			}
		})

		viewModel.endDisplay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { endText ->
				if(endText.isBlank())
					endDateButton.text = resources.getString(R.string.finish)
				else
					endDateButton.text = endText
			}
		})

		viewModel.containingDisplay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { contentText ->
				val fillText = if(contentText.isBlank()) resources.getString(R.string.any_text) else contentText
				containingButton.text = resources.getString(R.string.containing, fillText)
			}
		})

		viewModel.locationDisplay.observe(viewLifecycleOwner, Observer { value ->
			value?.let { locationText ->
				val fillText = if(locationText.isBlank()) resources.getString(R.string.any_location) else locationText
				locationButton.text = resources.getString(R.string.at, fillText)
			}
		})

		viewModel.tags.observe(viewLifecycleOwner, Observer { value ->
			tagChipGroup.removeAllViews()

			value?.let { list ->
				list.forEach { tag ->
					val displayTag = ChipWithId(tagChipGroup.context)
					displayTag.text = tag.name
					displayTag.itemId = tag.id
					displayTag.isCheckable = true
					tagChipGroup.addView(displayTag)
				}
			}
		})

		viewModel.displayNoTagsWarning.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				if(display) {
					noTagsWarning.show(true)
					tagChipGroup.show(false)
				} else {
					noTagsWarning.show(false)
					tagChipGroup.show(true)
				}
			}
		})

		viewModel.books.observe(viewLifecycleOwner, Observer { value ->
			bookChipGroup.removeAllViews()

			value?.let { list ->
				list.forEach { book ->
					val displayBook = ChipWithId(bookChipGroup.context)
					displayBook.text = book.name
					displayBook.itemId = book.id
					displayBook.isCheckable = true
					bookChipGroup.addView(displayBook)
				}
			}
		})

		viewModel.displayNoBooksWarning.observe(viewLifecycleOwner, Observer { value ->
			value?.let { display ->
				if(display) {
					noBooksWarning.show(true)
					bookChipGroup.show(false)
				} else {
					noBooksWarning.show(false)
					bookChipGroup.show(true)
				}
			}
		})

		viewModel.dismissBottomSheet.observe(viewLifecycleOwner, Observer { value ->
			value?.let { dismiss ->
				if(dismiss)
					criteriaBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
			}
		})

		viewModel.clearChipTicks.observe(viewLifecycleOwner, Observer { value ->
			value?.let { clear ->
				if(clear) {
					for(i in 0 until tagChipGroup.childCount) {
						val chip = tagChipGroup.getChildAt(i) as ChipWithId
						chip.isChecked = false
					}
					for(i in 0 until bookChipGroup.childCount) {
						val chip = bookChipGroup.getChildAt(i) as ChipWithId
						chip.isChecked = false
					}
				}
			}
		})

		viewModel.checkedTags.observe(viewLifecycleOwner, Observer { value ->
			value?.let { checkedTags ->
				for(i in 0 until tagChipGroup.childCount) {
					val chip = tagChipGroup.getChildAt(i) as ChipWithId
					chip.isChecked = checkedTags.contains(chip.itemId)
				}
			}
		})

		viewModel.checkedBooks.observe(viewLifecycleOwner, Observer { value ->
			value?.let { checkedBooks ->
				for(i in 0 until bookChipGroup.childCount) {
					val chip = bookChipGroup.getChildAt(i) as ChipWithId
					chip.isChecked = checkedBooks.contains(chip.itemId)
				}
			}
		})
	}

	private fun getCheckedTags(): List<Int> {
		val returnList = mutableListOf<Int>()
		for(i in 0 until tagChipGroup.childCount) {
			val chip = tagChipGroup.getChildAt(i) as ChipWithId
			if(chip.isChecked)
				returnList.add(chip.itemId)
		}
		return returnList
	}

	private fun getCheckedBooks(): List<Int> {
		val returnList = mutableListOf<Int>()
		for(i in 0 until bookChipGroup.childCount) {
			val chip = bookChipGroup.getChildAt(i) as ChipWithId
			if(chip.isChecked)
				returnList.add(chip.itemId)
		}
		return returnList
	}

	private fun onEntryClick(entryId: Int) {
		val viewEntryAction = SearchEntriesFragmentDirections.viewEntryAction()
		viewEntryAction.entryId = entryId
		Navigation.findNavController(requireView()).navigate(viewEntryAction)
	}
}
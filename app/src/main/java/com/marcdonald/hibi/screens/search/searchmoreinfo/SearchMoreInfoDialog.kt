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
package com.marcdonald.hibi.screens.search.searchmoreinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.*
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.addnewworddialog.AddNewWordDialog
import com.marcdonald.hibi.screens.search.searchmoreinfo.alternativesrecycler.SearchMoreInfoAlternativesRecyclerAdapter
import com.marcdonald.hibi.screens.search.searchmoreinfo.senserecycler.SearchMoreInfoSenseRecyclerAdapter
import timber.log.Timber

class SearchMoreInfoDialog : HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<SearchMoreInfoViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var quickAddImageView: ImageView
	private lateinit var mainWordDisplay: TextView
	private lateinit var mainReadingDisplay: TextView
	private lateinit var senseTitle: TextView
	private lateinit var alternativeTitle: TextView
	// </editor-fold>

	// <editor-fold desc="Recycler Views">
	private lateinit var alternativeRecycler: RecyclerView
	private lateinit var alternativesRecyclerAdapter: SearchMoreInfoAlternativesRecyclerAdapter
	private lateinit var senseRecycler: RecyclerView
	private lateinit var senseRecyclerAdapter: SearchMoreInfoSenseRecyclerAdapter
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let { arguments ->
			val japaneseListJson = arguments.getStringArrayList("japaneseList")
			val sensesListJson = arguments.getStringArrayList("sensesList")

			val entryId = arguments.getInt(ENTRY_ID_KEY)
			viewModel.passArguments(japaneseListJson as ArrayList<String>, sensesListJson as ArrayList<String>, entryId)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_search_more_info, container, false)
		bindViews(view)
		initAlternativesRecycler()
		initSenseRecycler()
		setupObservers()
		return view
	}

	private fun setupObservers() {
		viewModel.mainWord.observe(viewLifecycleOwner, Observer { value ->
			value?.let { word ->
				mainWordDisplay.text = resources.getString(R.string.japanese_word_wc, word)
			}
		})

		viewModel.mainReading.observe(viewLifecycleOwner, Observer { value ->
			value?.let { reading ->
				mainReadingDisplay.text = resources.getString(R.string.reading_wc, reading)
			}
		})

		viewModel.displayMainReading.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				mainReadingDisplay.show(shouldShow)
			}
		})

		viewModel.displayAlternatives.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				alternativeRecycler.show(shouldShow)
				alternativeTitle.show(shouldShow)
			}
		})

		viewModel.alternatives.observe(viewLifecycleOwner, Observer { value ->
			value?.let { list ->
				alternativesRecyclerAdapter.updateList(list)
			}
		})

		viewModel.displaySense.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				senseRecycler.show(shouldShow)
				senseTitle.show(shouldShow)
			}
		})

		viewModel.senseList.observe(viewLifecycleOwner, Observer { value ->
			value?.let { list ->
				senseRecyclerAdapter.updateList(list)
			}
		})

		viewModel.displayQuickAddWord.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				quickAddImageView.show(shouldShow)
			}
		})
	}

	private fun bindViews(view: View) {
		mainWordDisplay = view.findViewById(R.id.txt_search_more_info_main_word)
		mainWordDisplay.setOnClickListener(mainWordClickListener)

		mainReadingDisplay = view.findViewById(R.id.txt_search_more_info_main_reading)
		mainReadingDisplay.setOnClickListener(mainReadingClickListener)

		quickAddImageView = view.findViewById(R.id.img_quick_add_new_word)
		quickAddImageView.show(false)
		quickAddImageView.setOnClickListener(quickAddClickListener)

		senseTitle = view.findViewById(R.id.txt_search_more_info_sense_title)
		senseRecycler = view.findViewById(R.id.recycler_search_more_info_sense)

		alternativeTitle = view.findViewById(R.id.txt_search_more_info_alternative_title)
		alternativeRecycler = view.findViewById(R.id.recycler_search_more_info_alternative_japanese)
	}

	private val mainWordClickListener = View.OnClickListener {
		viewModel.mainWord.value?.let {
			val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
			val clip: ClipData = ClipData.newPlainText("Main Word", viewModel.mainWord.value)
			clipboard.setPrimaryClip(clip)

			val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, viewModel.mainWord.value)
			Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
		}
	}

	private val mainReadingClickListener = View.OnClickListener {
		viewModel.mainReading.value?.let {
			val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
			val clip: ClipData = ClipData.newPlainText("Main Reading", viewModel.mainReading.value)
			clipboard.setPrimaryClip(clip)

			val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, viewModel.mainReading.value)
			Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
		}
	}

	private val quickAddClickListener = View.OnClickListener {
		if(viewModel.entryId != 0) {
			val dialog = AddNewWordDialog()

			val bundle = Bundle()
			bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
			bundle.putString(NEW_WORD_QUICK_ADD, viewModel.mainWord.value)
			bundle.putString(NEW_WORD_READING_QUICK_ADD, viewModel.mainReading.value)

			bundle.putStringArrayList(NEW_WORD_PART_QUICK_ADD, viewModel.senseList.value?.get(0)?.partsOfSpeech as ArrayList<String>)
			bundle.putStringArrayList(NEW_WORD_MEANING_QUICK_ADD, viewModel.senseList.value?.get(0)?.englishDefinitions as ArrayList<String>)
			dialog.arguments = bundle

			dialog.show(requireFragmentManager(), "New Words Dialog")
		} else
			Timber.e("Log: quickAddClickListener: Tried to add new word when not in edit mode")
	}

	private fun initAlternativesRecycler() {
		this.alternativesRecyclerAdapter = SearchMoreInfoAlternativesRecyclerAdapter(requireContext())
		val layoutManager = LinearLayoutManager(context)
		alternativeRecycler.adapter = alternativesRecyclerAdapter
		alternativeRecycler.layoutManager = layoutManager

		val dividerItemDecoration = DividerItemDecoration(alternativeRecycler.context, layoutManager.orientation)
		dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_standard, null))
		alternativeRecycler.addItemDecoration(dividerItemDecoration)
	}

	private fun initSenseRecycler() {
		this.senseRecyclerAdapter = SearchMoreInfoSenseRecyclerAdapter(requireContext())
		val layoutManager = LinearLayoutManager(context)
		senseRecycler.adapter = senseRecyclerAdapter
		senseRecycler.layoutManager = layoutManager

		val dividerItemDecoration = DividerItemDecoration(senseRecycler.context, layoutManager.orientation)
		dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_standard, null))
		senseRecycler.addItemDecoration(dividerItemDecoration)
	}
}
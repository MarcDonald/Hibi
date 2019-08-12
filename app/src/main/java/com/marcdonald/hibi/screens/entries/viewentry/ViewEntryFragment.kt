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
package com.marcdonald.hibi.screens.entries.viewentry

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.internal.IS_EDIT_MODE_KEY
import com.marcdonald.hibi.internal.SEARCH_TERM_KEY
import com.marcdonald.hibi.internal.base.HibiFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.entries.ImageRecyclerAdapter
import com.marcdonald.hibi.screens.newwordsdialog.NewWordDialog
import com.marcdonald.hibi.screens.search.searchresults.SearchResultsDialog
import com.marcdonald.hibi.uicomponents.BinaryOptionDialog
import com.marcdonald.hibi.uicomponents.views.SearchBar

class ViewEntryFragment : HibiFragment() {

	private val viewModel by viewModels<ViewEntryViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var dateButton: MaterialButton
	private lateinit var timeButton: MaterialButton
	private lateinit var contentDisplay: TextView
	private lateinit var deleteConfirmDialog: BinaryOptionDialog
	private lateinit var searchBar: SearchBar
	private lateinit var tagDisplay: ChipGroup
	private lateinit var tagDisplayHolder: LinearLayout
	private lateinit var bookDisplay: ChipGroup
	private lateinit var bookDisplayHolder: LinearLayout
	private lateinit var newWordsButton: MaterialButton
	private lateinit var locationDisplay: TextView
	private lateinit var copyButton: FloatingActionButton
	private lateinit var scrollView: NestedScrollView
	private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_view_entry, container, false)

		bindViews(view)
		initDeleteConfirmDialog()
		setupObservers()
		setupImageRecycler(view)

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { arguments ->
			viewModel.passArguments(ViewEntryFragmentArgs.fromBundle(arguments).entryId)
		}

		// Has to start observing here since the argument needs to be passed first
		viewModel.images.observe(this, Observer { value ->
			value?.let { imagePaths ->
				imageRecyclerAdapter.updateItems(imagePaths)
			}
		})
	}

	private fun bindViews(view: View) {
		dateButton = view.findViewById(R.id.btn_view_date)
		timeButton = view.findViewById(R.id.btn_view_time)
		contentDisplay = view.findViewById(R.id.txt_view_content)
		tagDisplay = view.findViewById(R.id.cg_view_tags)
		tagDisplayHolder = view.findViewById(R.id.lin_view_tags)
		bookDisplay = view.findViewById(R.id.cg_view_books)
		bookDisplayHolder = view.findViewById(R.id.lin_view_books)
		copyButton = view.findViewById(R.id.fab_view_copy)
		copyButton.setOnClickListener { copyToClipboard() }

		scrollView = view.findViewById(R.id.scroll_view_entry)
		scrollView.setOnScrollChangeListener(scrollListener)

		if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			val metaScrollView = view.findViewById<NestedScrollView>(R.id.scroll_view_entry_meta)
			metaScrollView.setOnScrollChangeListener(scrollListener)
		}

		searchBar = view.findViewById(R.id.searchbar_view_entry)
		searchBar.setSearchAction(this::search)
		locationDisplay = view.findViewById(R.id.txt_view_location)

		val backButton: ImageView = view.findViewById(R.id.img_view_entry_toolbar_back)
		backButton.setOnClickListener(backClickListener)

		val editButton: ImageView = view.findViewById(R.id.img_edit)
		editButton.setOnClickListener(editClickListener)

		val deleteButton: ImageView = view.findViewById(R.id.img_delete)
		deleteButton.setOnClickListener(deleteClickListener)

		newWordsButton = view.findViewById(R.id.btn_view_new_words)
		newWordsButton.setOnClickListener(newWordsClickListener)
	}

	private fun setupObservers() {
		viewModel.displayErrorToast.observe(this, Observer { value ->
			value?.let { show ->
				if(show)
					Toast.makeText(requireContext(), resources.getString(R.string.generic_error), Toast.LENGTH_SHORT).show()
			}
		})

		viewModel.content.observe(this, Observer { value ->
			value?.let { content ->
				contentDisplay.text = content
			}
		})

		viewModel.readableDate.observe(this, Observer { value ->
			value?.let { date ->
				dateButton.text = date
			}
		})

		viewModel.readableTime.observe(this, Observer { value ->
			value?.let { time ->
				timeButton.text = time
			}
		})

		viewModel.tags.observe(this, Observer { value ->
			tagDisplay.removeAllViews()

			value?.let { tags ->
				tagDisplayHolder.show(tags.isNotEmpty())
				tags.forEach { tag ->
					val displayTag = Chip(tagDisplay.context)
					displayTag.ellipsize = TextUtils.TruncateAt.END
					displayTag.text = tag.name
					tagDisplay.addView(displayTag)
				}
			}
		})

		viewModel.books.observe(this, Observer { value ->
			bookDisplay.removeAllViews()

			value?.let { books ->
				bookDisplayHolder.show(books.isNotEmpty())
				books.forEach { book ->
					val displayBook = Chip(bookDisplay.context)
					displayBook.ellipsize = TextUtils.TruncateAt.END
					displayBook.text = book.name
					bookDisplay.addView(displayBook)
				}
			}
		})

		viewModel.displayNewWordButton.observe(this, Observer { value ->
			value?.let { shouldShow ->
				newWordsButton.show(shouldShow)
			}
		})

		viewModel.popBackStack.observe(this, Observer { value ->
			value?.let { pop ->
				if(pop)
					Navigation.findNavController(requireView()).popBackStack()
			}
		})

		viewModel.location.observe(this, Observer { value ->
			value?.let { location ->
				locationDisplay.show(location.isNotBlank())
				locationDisplay.text = location
			}
		})
	}

	private fun setupImageRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_view_entry_images)
		this.imageRecyclerAdapter = ImageRecyclerAdapter(::onImageClick, {}, requireContext(), requireActivity().theme)

		val layoutManager = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
			GridLayoutManager(context, 3)
		else
			LinearLayoutManager(context)

		recycler.adapter = imageRecyclerAdapter
		recycler.layoutManager = layoutManager
	}

	private fun onImageClick(imagePath: String) {
		val fullScreenImageAction = ViewEntryFragmentDirections.fullscreenImageAction(imagePath)
		Navigation.findNavController(requireView()).navigate(fullScreenImageAction)
	}

	private val backClickListener = View.OnClickListener {
		Navigation.findNavController(view!!).popBackStack()
	}

	private val editClickListener = View.OnClickListener {
		val editEntryAction = ViewEntryFragmentDirections.editEntryAction()
		if(viewModel.entryId != 0) {
			editEntryAction.entryId = viewModel.entryId
		}
		Navigation.findNavController(it).navigate(editEntryAction)
	}

	private val deleteClickListener = View.OnClickListener {
		deleteConfirmDialog.show(requireFragmentManager(), "Delete Confirmation Dialog")
	}

	private val newWordsClickListener = View.OnClickListener {
		val dialog = NewWordDialog()

		val bundle = Bundle()
		bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
		bundle.putBoolean(IS_EDIT_MODE_KEY, false)
		dialog.arguments = bundle

		dialog.show(requireFragmentManager(), "View New Words Dialog")
	}

	private fun search(searchTerm: String) {
		val args = Bundle()
		args.putString(SEARCH_TERM_KEY, searchTerm)

		val searchDialog = SearchResultsDialog()
		searchDialog.arguments = args

		searchDialog.show(requireFragmentManager(), "View Entry Search")
	}

	private fun initDeleteConfirmDialog() {
		deleteConfirmDialog = BinaryOptionDialog()
		deleteConfirmDialog.setTitle(resources.getString(R.string.delete_confirm_title))
		deleteConfirmDialog.setMessage(resources.getString(R.string.delete_confirm))
		deleteConfirmDialog.setNegativeButton(resources.getString(R.string.delete), okDeleteClickListener)
		deleteConfirmDialog.setPositiveButton(resources.getString(R.string.cancel), cancelDeleteClickListener)
	}

	private val okDeleteClickListener = View.OnClickListener {
		viewModel.deleteEntry()
		deleteConfirmDialog.dismiss()
	}

	private val cancelDeleteClickListener = View.OnClickListener {
		deleteConfirmDialog.dismiss()
	}

	private val scrollListener = NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
		if(scrollY > oldScrollY) {
			copyButton.hide()
		} else {
			copyButton.show()
		}
	}

	private fun copyToClipboard() {
		val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip: ClipData = ClipData.newPlainText("Entry", contentDisplay.text.toString())
		clipboard.primaryClip = clip
		Toast.makeText(requireContext(), resources.getString(R.string.copied_entry_to_clipboard), Toast.LENGTH_SHORT).show()
	}
}
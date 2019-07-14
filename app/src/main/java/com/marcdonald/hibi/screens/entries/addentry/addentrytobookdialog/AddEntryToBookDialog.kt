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
package com.marcdonald.hibi.screens.entries.addentry.addentrytobookdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.addbookdialog.AddBookDialog
import com.marcdonald.hibi.uicomponents.views.CheckBoxWithId

class AddEntryToBookDialog : HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<AddEntryToBookViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var title: TextView
	private lateinit var bookHolder: LinearLayout
	private lateinit var noBooksWarning: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			viewModel.passArguments(requireArguments().getInt(ENTRY_ID_KEY, 0))
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_entry_books, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_add_entry_to_book_title)
		bookHolder = view.findViewById(R.id.lin_add_entry_to_book_book_holder)
		noBooksWarning = view.findViewById(R.id.txt_no_books_warning)

		val addButton: MaterialButton = view.findViewById(R.id.btn_create_book)
		addButton.setOnClickListener {
			val dialog = AddBookDialog()
			dialog.show(requireFragmentManager(), "Book Manager Dialog")
		}

		val saveButton: MaterialButton = view.findViewById(R.id.btn_save_book)
		saveButton.setOnClickListener(saveClickListener)
	}

	private fun setupObservers() {
		viewModel.allBooks.observe(this, Observer { value ->
			value?.let { books ->
				viewModel.listReceived(books.isEmpty())

				books.forEach { book ->
					// Gets list of all books currently displayed
					val alreadyDisplayedBooks = ArrayList<CheckBoxWithId>()
					for(x in 0 until bookHolder.childCount) {
						val bookCheckBox = bookHolder.getChildAt(x) as CheckBoxWithId
						alreadyDisplayedBooks.add(bookCheckBox)
					}

					val displayBook = CheckBoxWithId(bookHolder.context)
					displayBook.text = book.name
					displayBook.itemId = book.id
					if(theme == R.style.Theme_Hibi_BottomSheetDialog_Dark) {
						displayBook.setTextColor(resources.getColor(R.color.darkThemePrimaryText, null))
					} else {
						displayBook.setTextColor(resources.getColor(R.color.lightThemePrimaryText, null))
					}

					// If the new book is already displayed, don't add it
					// This stops it removing user progress before saving
					var addIt = true
					alreadyDisplayedBooks.forEach { alreadyDisplayedBook ->
						if(alreadyDisplayedBook.itemId == displayBook.itemId) {
							addIt = false
						}
					}

					if(addIt)
						bookHolder.addView(displayBook)
				}
			}
		})

		viewModel.displayNoBookWarning.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noBooksWarning.show(shouldShow)
			}
		})

		viewModel.bookEntryRelations.observe(this, Observer { value ->
			value?.let { bookEntryRelations ->
				bookEntryRelations.forEach { bookId ->
					for(x in 0 until bookHolder.childCount) {
						val bookCheckBox = bookHolder.getChildAt(x) as CheckBoxWithId
						if(bookCheckBox.itemId == bookId)
							bookCheckBox.isChecked = true
					}
				}
			}
		})

		viewModel.dismiss.observe(this, Observer { value ->
			value?.let { dismiss ->
				if(dismiss)
					dismiss()
			}
		})
	}

	private val saveClickListener = View.OnClickListener {
		val list = arrayListOf<Pair<Int, Boolean>>()
		for(x in 0 until bookHolder.childCount) {
			val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
			list.add(Pair(checkBox.itemId, checkBox.isChecked))
		}
		viewModel.onSaveClick(list)
	}
}
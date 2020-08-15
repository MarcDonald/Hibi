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
package com.marcdonald.hibi.screens.multiselectdialog.addmultientrytobookdialog

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
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.addbookdialog.AddBookDialog
import com.marcdonald.hibi.uicomponents.views.CheckBoxWithId

class AddMultiEntryToBookDialog(private val selectedCount: Int,
																private val onSaveClick: (Boolean, List<Int>) -> Unit)
	: HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<AddMultiEntryToBookViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var title: TextView
	private lateinit var bookHolder: LinearLayout
	private lateinit var noBooksWarning: TextView
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_multi_entry_books, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_add_books_multi_title)
		title.text = resources.getQuantityString(R.plurals.multi_book_title, selectedCount, selectedCount)
		bookHolder = view.findViewById(R.id.lin_books_entry_multi_books_holder)
		noBooksWarning = view.findViewById(R.id.txt_no_books_multi_warning)

		val addButton: MaterialButton = view.findViewById(R.id.btn_add_book_multi)
		addButton.setOnClickListener {
			val dialog = AddBookDialog()
			dialog.show(requireFragmentManager(), "Book Manager Dialog")
		}

		val saveButton: MaterialButton = view.findViewById(R.id.btn_save_books_multi)
		saveButton.setOnClickListener(saveClickListener)

		val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_books_multi)
		deleteButton.setOnClickListener(deleteClickListener)

	}

	private fun setupObservers() {
		viewModel.allBooks.observe(viewLifecycleOwner, Observer { value ->
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

		viewModel.displayNoBooksWarning.observe(viewLifecycleOwner, Observer { value ->
			value?.let { shouldShow ->
				noBooksWarning.show(shouldShow)
			}
		})
	}

	private val saveClickListener = View.OnClickListener {
		val list = mutableListOf<Int>()
		for(x in 0 until bookHolder.childCount) {
			val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
			if(checkBox.isChecked)
				list.add(checkBox.itemId)
		}
		onSaveClick(false, list)
		dismiss()
	}

	private val deleteClickListener = View.OnClickListener {
		val list = mutableListOf<Int>()
		for(x in 0 until bookHolder.childCount) {
			val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
			if(checkBox.isChecked)
				list.add(checkBox.itemId)
		}
		onSaveClick(true, list)
		dismiss()
	}
}
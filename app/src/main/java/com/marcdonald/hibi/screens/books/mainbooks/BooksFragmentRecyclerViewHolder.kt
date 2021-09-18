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
package com.marcdonald.hibi.screens.books.mainbooks

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.BOOK_ID_KEY
import com.marcdonald.hibi.screens.addbookdialog.AddBookDialog
import com.marcdonald.hibi.screens.main.MainScreenFragmentDirections

class BooksFragmentRecyclerViewHolder(itemView: View, private val fragmentManager: FragmentManager) :
		RecyclerView.ViewHolder(itemView) {

	private var bookNameDisplay: TextView = itemView.findViewById(R.id.book_item_name)
	private var bookCountDisplay: TextView = itemView.findViewById(R.id.book_item_count)

	private var displayedItem: BookDisplayItem? = null

	private val clickListener = View.OnClickListener {
		if(displayedItem != null) {
			val bookID = displayedItem!!.bookId
			val viewEntryAction = MainScreenFragmentDirections.viewBookEntriesAction(bookID)
			Navigation.findNavController(itemView).navigate(viewEntryAction)
		}
	}

	private val longClickListener = View.OnLongClickListener {
		initEditOrDeleteDialog()
		true
	}

	private fun initEditOrDeleteDialog() {
		val editBookDialog = AddBookDialog()
		if(displayedItem != null) {
			val arguments = Bundle()
			arguments.putInt(BOOK_ID_KEY, displayedItem!!.bookId)
			editBookDialog.arguments = arguments
		}
		editBookDialog.show(fragmentManager, "Edit or Delete Book Dialog")
	}

	init {
		itemView.setOnClickListener(clickListener)
		itemView.setOnLongClickListener(longClickListener)
	}

	fun display(item: BookDisplayItem) {
		displayedItem = item
		bookNameDisplay.text = item.bookName
		bookCountDisplay.text = itemView.resources.getString(R.string.book_use_count_wc, item.useCount.toString())
	}
}
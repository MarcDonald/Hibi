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
package com.marcdonald.hibi.screens.tagsscreen.maintagsfragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.TAG_ID_KEY
import com.marcdonald.hibi.screens.mainscreen.MainScreenFragmentDirections
import com.marcdonald.hibi.uicomponents.addtagdialog.AddTagDialog

class TagsFragmentRecyclerViewHolder(itemView: View,
																		 private val fragmentManager: FragmentManager)
	: RecyclerView.ViewHolder(itemView) {

	private var tagNameDisplay: TextView = itemView.findViewById(R.id.tag_item_name)
	private var tagCountDisplay: TextView = itemView.findViewById(R.id.tag_item_count)

	private var displayedItem: TagDisplayItem? = null

	private val clickListener = View.OnClickListener {
		if(displayedItem != null) {
			val tagID = displayedItem!!.tagID
			val viewEntryAction = MainScreenFragmentDirections.viewTaggedEntriesAction(tagID)
			Navigation.findNavController(itemView).navigate(viewEntryAction)
		}
	}

	private val longClickListener = View.OnLongClickListener {
		initEditOrDeleteDialog()
		true
	}

	private fun initEditOrDeleteDialog() {
		val editTagDialog = AddTagDialog()
		if(displayedItem != null) {
			val arguments = Bundle()
			arguments.putInt(TAG_ID_KEY, displayedItem!!.tagID)
			editTagDialog.arguments = arguments
		}
		editTagDialog.show(fragmentManager, "Edit or Delete Tag Dialog")
	}

	init {
		itemView.setOnClickListener(clickListener)
		itemView.setOnLongClickListener(longClickListener)
	}

	fun display(item: TagDisplayItem) {
		displayedItem = item
		tagNameDisplay.text = item.tagName
		tagCountDisplay.text = itemView.resources.getString(R.string.tag_use_count_wc, item.useCount.toString())
	}
}
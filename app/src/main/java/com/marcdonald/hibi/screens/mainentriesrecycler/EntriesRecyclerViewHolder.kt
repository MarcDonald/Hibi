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
package com.marcdonald.hibi.screens.mainentriesrecycler

import android.content.res.Resources
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_BOOKS
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_LOCATION
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_TAGS
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class EntriesRecyclerViewHolder(private val onClick: (Int) -> Unit,
																private val onSelectClick: View.OnClickListener?,
																itemView: View,
																private val theme: Resources.Theme)
	: BaseEntriesRecyclerViewHolder(itemView), KodeinAware {

	override val kodein: Kodein by closestKodein(itemView.context)
	private val dateTimeUtils: DateTimeUtils by instance()

	// <editor-fold desc="UI Components">
	private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
	private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
	private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
	private var tagDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_tags)
	private var bookDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_books)
	private var locationDisplay: TextView = itemView.findViewById(R.id.txt_item_location)
	// </editor-fold>

	// <editor-fold desc="Other">
	private var displayedItem: MainEntriesDisplayItem? = null
	private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)
	// </editor-fold>

	private val clickListener = View.OnClickListener {
		displayedItem?.let { item ->
			onClick(item.entry.id)
		}
	}

	init {
		itemView.findViewById<ConstraintLayout>(R.id.const_item_main_recycler).setOnClickListener(clickListener)
	}

	override fun display(item: MainEntriesDisplayItem) {
		this.displayedItem = item
		val dateDisplayText = dateTimeUtils.formatDateForDisplay(item.entry.day, item.entry.month, item.entry.year)
		dateDisplay.text = dateDisplayText
		val timeDisplayText = dateTimeUtils.formatTimeForDisplay(item.entry.hour, item.entry.minute)
		timeDisplay.text = timeDisplayText
		contentDisplay.text = item.entry.content
		displayLocation()
		displayTags()
		displayBooks()

		if(item.isSelected) {
			val typedValue = TypedValue()
			theme.resolveAttribute(R.attr.hibiSelectedItemColor, typedValue, true)
			itemView.setBackgroundColor(typedValue.data)
		} else {
			itemView.background = null
		}
		val selectedIcon: ImageView = itemView.findViewById(R.id.img_item_selected)
		selectedIcon.show(item.isSelected)
		selectedIcon.setOnClickListener(onSelectClick)

		val favouritedIcon: ImageView = itemView.findViewById(R.id.img_item_favourited)
		favouritedIcon.show(item.entry.isFavourite)
	}

	private fun displayLocation() {
		if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_LOCATION, true)) {
			displayedItem?.let { item ->
				if(item.entry.location.isNotBlank()) {
					locationDisplay.text = item.entry.location
					locationDisplay.show(true)
				} else {
					locationDisplay.show(false)
				}
			}
		} else {
			locationDisplay.show(false)
		}
	}

	private fun displayTags() {
		if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_TAGS, true)) {
			displayedItem?.let { item ->
				tagDisplay.show(item.tags.isNotEmpty())
				val tagChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_tags)

				tagChipGroup.removeAllViews()
				if(item.tags.isNotEmpty()) {
					item.tags.forEach { tagName ->
						val chip = Chip(itemView.context)
						chip.ellipsize = TextUtils.TruncateAt.END
						chip.text = tagName
						tagChipGroup.addView(chip)
					}
				}
			}
		} else {
			tagDisplay.show(false)
		}
	}

	private fun displayBooks() {
		if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_BOOKS, true)) {
			displayedItem?.let { item ->
				bookDisplay.show(item.books.isNotEmpty())
				val bookChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_books)

				bookChipGroup.removeAllViews()
				if(item.books.isNotEmpty()) {
					item.books.forEach { bookName ->
						val chip = Chip(itemView.context)
						chip.ellipsize = TextUtils.TruncateAt.END
						chip.text = bookName
						bookChipGroup.addView(chip)
					}
				}
			}
		} else {
			bookDisplay.show(false)
		}
	}
}
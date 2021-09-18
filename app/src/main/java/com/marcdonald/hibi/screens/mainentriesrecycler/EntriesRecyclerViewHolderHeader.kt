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
package com.marcdonald.hibi.screens.mainentriesrecycler

import android.view.View
import android.widget.TextView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class EntriesRecyclerViewHolderHeader(itemView: View) : BaseEntriesRecyclerViewHolder(itemView),
																												KodeinAware {

	override val kodein: Kodein by closestKodein(itemView.context)
	private val dateTimeUtils: DateTimeUtils by instance()

	// Entry
	private var displayedItem: MainEntriesDisplayItem? = null

	// UI Components
	private val dateDisplay: TextView = itemView.findViewById(R.id.txt_header_date)

	override fun display(item: MainEntriesDisplayItem) {
		this.displayedItem = item
		dateDisplay.text = dateTimeUtils.formatDateForHeader(item.entry.month, item.entry.year)
	}
}
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
package com.marcdonald.hibi.internal.utils

import android.content.Context
import android.preference.PreferenceManager
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.internal.PREF_DATE_HEADER_PERIOD
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem

class EntryDisplayUtilsImpl(private val context: Context) : EntryDisplayUtils {
	override fun convertToMainEntriesDisplayItemList(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>, bookEntryDisplayItems: List<BookEntryDisplayItem>): List<MainEntriesDisplayItem> {
		val itemList = ArrayList<MainEntriesDisplayItem>()

		entries.forEach { entry ->
			val item = MainEntriesDisplayItem(entry, listOf(), listOf())
			val listOfTags = ArrayList<String>()
			val listOfBooks = ArrayList<String>()

			tagEntryDisplayItems.forEach { tagEntryDisplayItem ->
				if(tagEntryDisplayItem.entryId == entry.id) {
					listOfTags.add(tagEntryDisplayItem.tagName)
				}
			}

			bookEntryDisplayItems.forEach { bookEntryDisplayItem ->
				if(bookEntryDisplayItem.entryId == entry.id) {
					listOfBooks.add(bookEntryDisplayItem.bookName)
				}
			}

			item.tags = listOfTags
			item.books = listOfBooks
			itemList.add(item)
		}
		return itemList
	}

	override fun convertToMainEntriesDisplayItemListWithDateHeaders(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>, bookEntryDisplayItems: List<BookEntryDisplayItem>): List<MainEntriesDisplayItem> {
		return addListHeaders(convertToMainEntriesDisplayItemList(entries, tagEntryDisplayItems, bookEntryDisplayItems))
	}

	private fun addListHeaders(allItems: List<MainEntriesDisplayItem>): List<MainEntriesDisplayItem> {
		return when(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_DATE_HEADER_PERIOD, "1")) {
			"0"  -> allItems
			"1"  -> addMonthHeaders(allItems)
			"2"  -> addYearHeaders(allItems)
			else -> allItems
		}
	}

	private fun addMonthHeaders(allItems: List<MainEntriesDisplayItem>): List<MainEntriesDisplayItem> {
		val listWithHeaders = mutableListOf<MainEntriesDisplayItem>()
		listWithHeaders.addAll(allItems)

		var lastMonth = 12
		var lastYear = 9999
		if(allItems.isNotEmpty()) {
			lastMonth = allItems.first().entry.month + 1
			lastYear = allItems.first().entry.year
		}

		val headersToAdd = mutableListOf<Pair<Int, MainEntriesDisplayItem>>()

		for(x in allItems.indices) {
			if(((allItems[x].entry.month < lastMonth) && (allItems[x].entry.year == lastYear))
				 || (allItems[x].entry.month > lastMonth) && (allItems[x].entry.year < lastYear)
				 || (allItems[x].entry.year < lastYear)
			) {
				val header = Entry(0, allItems[x].entry.month, allItems[x].entry.year, 0, 0, "")
				val headerItem = MainEntriesDisplayItem(header, listOf(), listOf())
				lastMonth = allItems[x].entry.month
				lastYear = allItems[x].entry.year
				headersToAdd.add(Pair(x, headerItem))
			}
		}

		for((add, x) in (0 until headersToAdd.size).withIndex()) {
			listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
		}

		return listWithHeaders
	}

	private fun addYearHeaders(allItems: List<MainEntriesDisplayItem>): List<MainEntriesDisplayItem> {
		val listWithHeaders = mutableListOf<MainEntriesDisplayItem>()
		listWithHeaders.addAll(allItems)

		var lastYear = 9999
		if(allItems.isNotEmpty()) {
			lastYear = allItems.first().entry.year + 1
		}

		val headersToAdd = mutableListOf<Pair<Int, MainEntriesDisplayItem>>()

		for(x in allItems.indices) {
			if(allItems[x].entry.year < lastYear) {
				val header = Entry(0, allItems[x].entry.month, allItems[x].entry.year, 0, 0, "")
				val headerItem = MainEntriesDisplayItem(header, listOf(), listOf())
				lastYear = allItems[x].entry.year
				headersToAdd.add(Pair(x, headerItem))
			}
		}

		for((add, x) in (0 until headersToAdd.size).withIndex()) {
			listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
		}

		return listWithHeaders
	}
}
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
package com.marcdonald.hibi.internal.utils

import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.MainEntriesDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem

interface EntryDisplayUtils {
	fun convertToMainEntriesDisplayItemList(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>, bookEntryDisplayItems: List<BookEntryDisplayItem>): List<MainEntriesDisplayItem>

	fun convertToMainEntriesDisplayItemListWithDateHeaders(entries: List<Entry>, tagEntryDisplayItems: List<TagEntryDisplayItem>, bookEntryDisplayItems: List<BookEntryDisplayItem>): List<MainEntriesDisplayItem>
}
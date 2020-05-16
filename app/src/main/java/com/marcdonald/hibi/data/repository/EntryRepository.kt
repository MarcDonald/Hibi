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
package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.NumberAndDateObject
import com.marcdonald.hibi.data.entity.Entry
import java.util.*

interface EntryRepository {

	suspend fun addEntry(entry: Entry)

	suspend fun saveEntry(id: Int, day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String)

	suspend fun getEntry(id: Int): Entry

	suspend fun getAllEntries(): List<Entry>

	suspend fun deleteEntry(id: Int)

	suspend fun getLastEntryId(): Int

	suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry>

	suspend fun getEntriesOnDate(calendar: Calendar): List<Entry>

	suspend fun getEntriesOnDate(calendar: Calendar, ascending: Boolean): List<Entry>

	suspend fun saveLocation(entryId: Int, location: String)

	suspend fun getLocation(entryId: Int): String

	fun getLocationLD(entryId: Int): LiveData<String>

	suspend fun getAllYears(): List<Int>

	suspend fun getFirstEntryOnDate(calendar: Calendar): Entry?

	suspend fun getAmountOfEntriesOnDate(calendar: Calendar): Int

	suspend fun getFavouriteEntries(): List<Entry>

	suspend fun setEntryIsFavourite(id: Int, isFavourite: Boolean)

	val entryCount: LiveData<Int>

	val favouritesCount: LiveData<Int>

	val dayCount: LiveData<Int>

	val locationCount: LiveData<Int>

	suspend fun getMostEntriesInOneDay(): NumberAndDateObject
}
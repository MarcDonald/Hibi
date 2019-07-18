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
package com.marcdonald.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.internal.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class EntryRepositoryImpl private constructor(private val dao: DAO, private val fileUtils: FileUtils) :
		EntryRepository {

	override suspend fun addEntry(entry: Entry) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertEntry(entry)
				Timber.d("Log: addEntry: Entry doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addEntry: Entry already exists, updating existing")
				dao.updateEntry(entry)
			}
		}
	}

	override suspend fun saveEntry(id: Int, day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String) {
		withContext(Dispatchers.IO) {
			dao.saveEntry(id, day, month, year, hour, minute, content)
		}
	}

	override suspend fun getAllEntries(): List<Entry> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAllEntries()
		}
	}

	override suspend fun getEntry(id: Int): Entry {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getEntry(id)
		}
	}

	override suspend fun deleteEntry(id: Int) {
		withContext(Dispatchers.IO) {
			val entryImages = dao.getImagesForEntry(id)
			entryImages.forEach { entryImage ->
				val count = dao.countUsesOfImage(entryImage.imageName)
				Timber.i("Log: deleteEntry: $count")
				if(count <= 1) {
					fileUtils.deleteImage(entryImage.imageName)
				}
			}
			dao.deleteEntry(id)
		}
	}

	override suspend fun getLastEntryId(): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getLastEntryId()
		}
	}

	override suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getEntriesOnDate(year, month, day)
		}
	}

	override suspend fun getEntriesOnDate(calendar: Calendar): List<Entry> {
		return withContext(Dispatchers.IO) {
			return@withContext getEntriesOnDate(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
			)
		}
	}

	override suspend fun saveLocation(entryId: Int, location: String) {
		withContext(Dispatchers.IO) {
			dao.setLocation(entryId, location)
		}
	}

	override suspend fun getLocation(entryId: Int): String {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getLocation(entryId)
		}
	}

	override fun getLocationLD(entryId: Int): LiveData<String> {
		return dao.getLocationLD(entryId)
	}

	override suspend fun getAllYears(): List<Int> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAllYears()
		}
	}

	override suspend fun getFirstEntryOnDate(calendar: Calendar): Entry {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getFirstEntryOnDate(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
			)
		}
	}

	override suspend fun getAmountOfEntriesOnDate(calendar: Calendar): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAmountOfEntriesOnDate(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
			)
		}
	}

	override suspend fun getEntriesOnDate(calendar: Calendar, ascending: Boolean): List<Entry> {
		if(ascending) {
			return withContext(Dispatchers.IO) {
				return@withContext dao.getEntriesOnDateAscending(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)
				)
			}
		} else {
			return getEntriesOnDate(calendar)
		}
	}

	override suspend fun getFavouriteEntries(): List<Entry> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getFavouriteEntries()
		}
	}

	override suspend fun setEntryIsFavourite(id: Int, isFavourite: Boolean) {
		withContext(Dispatchers.IO) {
			dao.setEntryIsFavourite(id, isFavourite)
		}
	}

	override val entryCount: LiveData<Int>
		get() = dao.getCountEntries()

	override val favouritesCount: LiveData<Int>
		get() = dao.getCountFavourites()

	companion object {
		@Volatile private var instance: EntryRepositoryImpl? = null

		fun getInstance(dao: DAO, fileUtils: FileUtils) =
			instance ?: synchronized(this) {
				instance ?: EntryRepositoryImpl(dao, fileUtils).also { instance = it }
			}
	}
}
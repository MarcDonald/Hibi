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

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.EntryImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class EntryImageRepositoryImpl(private val dao: DAO) : EntryImageRepository {
	override suspend fun addEntryImage(entryImage: EntryImage) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertEntryImage(entryImage)
				Timber.d("Log: addEntryImage: EntryImage doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addEntryImage: EntryImage already exists, updating existing")
				dao.updateEntryImage(entryImage)
			}
		}
	}

	override suspend fun deleteEntryImage(entryImage: EntryImage) {
		withContext(Dispatchers.IO) {
			dao.deleteEntryImage(entryImage)
		}
	}

	override fun getImagesForEntry(entryId: Int): LiveData<List<EntryImage>> {
		return dao.getImagesForEntryLD(entryId)
	}

	override suspend fun countUsesOfImage(imageName: String): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.countUsesOfImage(imageName)
		}
	}

	override fun getCountImagesForEntry(entryId: Int): LiveData<Int> {
		return dao.getCountImagesForEntry(entryId)
	}

	companion object {
		@Volatile private var instance: EntryImageRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: EntryImageRepositoryImpl(dao).also { instance = it }
			}
	}
}
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
import com.marcdonald.hibi.data.database.NumberAndDateObject
import com.marcdonald.hibi.data.database.NumberAndIdObject
import com.marcdonald.hibi.data.entity.NewWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewWordRepositoryImpl private constructor(private val dao: DAO) : NewWordRepository {

	override suspend fun addNewWord(newWord: NewWord) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertNewWord(newWord)
				Timber.d("Log: addNewWord: NewWord doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addNewWord: NewWord already exists, updating existing")
				dao.updateNewWord(newWord)
			}
		}
	}

	override suspend fun getNewWord(id: Int): NewWord {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getNewWord(id)
		}
	}

	override suspend fun deleteNewWord(id: Int) {
		withContext(Dispatchers.IO) {
			dao.deleteNewWord(id)
		}
	}

	override fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>> {
		return dao.getNewWordsByEntryId(entryId)
	}

	override suspend fun getNewWordCountByEntryId(entryId: Int): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getNewWordCountByEntryId(entryId)
		}
	}

	override fun getNewWordCountByEntryIdLD(entryId: Int): LiveData<Int> {
		return dao.getNewWordCountByEntryIdLD(entryId)
	}

	override val newWordCount: LiveData<Int>
		get() = dao.getCountNewWords()

	override suspend fun getMostNewWordsInOneDay(): NumberAndDateObject {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getMostNewWordsInOneDay()
		}
	}

	override suspend fun getMostNewWordsInOneEntry(): NumberAndIdObject {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getMostNewWordsInOneEntry()
		}
	}

	companion object {
		@Volatile private var instance: NewWordRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: NewWordRepositoryImpl(dao).also { instance = it }
			}
	}
}
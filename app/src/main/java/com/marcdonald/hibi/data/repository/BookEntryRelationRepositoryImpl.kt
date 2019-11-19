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
import com.marcdonald.hibi.data.entity.Book
import com.marcdonald.hibi.data.entity.BookEntryRelation
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.screens.books.mainbooks.BookDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookEntryRelationRepositoryImpl private constructor(private val dao: DAO) :
		BookEntryRelationRepository {

	override suspend fun addBookEntryRelation(bookEntryRelation: BookEntryRelation) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertBookEntryRelation(bookEntryRelation)
				Timber.d("Log: addBookEntryRelation: BookEntryRelation doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addBookEntryRelation: BookEntryRelation already exists, updating existing")
				dao.updateBookEntryRelation(bookEntryRelation)
			}
		}
	}

	override suspend fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation) {
		withContext(Dispatchers.IO) {
			dao.deleteBookEntryRelation(bookEntryRelation)
		}
	}

	override suspend fun getEntriesWithBook(bookId: Int): List<Entry> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getEntriesWithBook(bookId)
		}
	}

	override val bookDisplayItems: LiveData<List<BookDisplayItem>> by lazy {
		dao.getBookDisplayItems()
	}

	override suspend fun getBookIdsWithEntry(entryId: Int): List<Int> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBookIdsWithEntry(entryId)
		}
	}

	override suspend fun getBooksWithEntry(entryId: Int): List<Book> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBooksWithEntry(entryId)
		}
	}

	override suspend fun getAllBookEntryRelationsWithIds(ids: List<Int>): List<BookEntryRelation> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAllBookEntryRelationsWithIds(ids)
		}
	}

	override fun getCountBooksWithEntryLD(entryId: Int): LiveData<Int> {
		return dao.getCountBooksWithEntry(entryId)
	}

	override suspend fun getBookEntryDisplayItems(): List<BookEntryDisplayItem> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBookEntryDisplayItems()
		}
	}

	override val entriesInBooksCount: LiveData<Int>
		get() = dao.getCountEntriesInBooks()

	override suspend fun getBookWithMostEntries(): Book? {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBookWithMostEntries()
		}
	}

	companion object {
		@Volatile private var instance: BookEntryRelationRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: BookEntryRelationRepositoryImpl(dao).also { instance = it }
			}
	}
}
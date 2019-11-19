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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookRepositoryImpl private constructor(private val dao: DAO) : BookRepository {

	override suspend fun addBook(book: Book) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertBook(book)
				Timber.d("Log: addBook: Book doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addBook: Book already exists, updating existing")
				dao.updateBook(book)
			}
		}
	}

	override suspend fun getBook(bookId: Int): Book {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBookById(bookId)
		}
	}

	override suspend fun deleteBook(bookId: Int) {
		withContext(Dispatchers.IO) {
			dao.deleteBook(bookId)
		}
	}

	override suspend fun isBookNameInUse(name: String): Boolean {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getCountBooksWithName(name) > 0
		}
	}

	override suspend fun getBookName(bookId: Int): String {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getBookName(bookId)
		}
	}

	override fun getAllBooksLD(): LiveData<List<Book>> {
		return dao.getAllBooksLD()
	}

	override suspend fun getAllBooks(): List<Book> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAllBooks()
		}
	}

	companion object {
		@Volatile private var instance: BookRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: BookRepositoryImpl(dao).also { instance = it }
			}
	}
}
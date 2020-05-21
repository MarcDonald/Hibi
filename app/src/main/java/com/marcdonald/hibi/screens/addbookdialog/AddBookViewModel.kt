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
package com.marcdonald.hibi.screens.addbookdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Book
import com.marcdonald.hibi.data.repository.BookRepository
import kotlinx.coroutines.launch

class AddBookViewModel(private val bookRepository: BookRepository) : ViewModel() {
	private var _bookId = 0
	val bookId: Int
		get() = _bookId

	private val _bookTitle = MutableLiveData<String>()
	val bookTitle: LiveData<String>
		get() = _bookTitle

	private val _isEditMode = MutableLiveData<Boolean>()
	val isEditMode: LiveData<Boolean>
		get() = _isEditMode

	private val _displayEmptyContentWarning = MutableLiveData<Boolean>()
	val displayEmptyContentWarning: LiveData<Boolean>
		get() = _displayEmptyContentWarning

	private val _displayDuplicateNameWarning = MutableLiveData<Boolean>()
	val displayDuplicateNameWarning: LiveData<Boolean>
		get() = _displayDuplicateNameWarning

	private val _dismiss = MutableLiveData<Boolean>()
	val dismiss: LiveData<Boolean>
		get() = _dismiss

	fun passArguments(bookIdArg: Int) {
		_bookId = bookIdArg
		if(bookId != 0) {
			_isEditMode.value = true
			getBookName()
		}
	}

	fun saveBook(input: String) {
		viewModelScope.launch {
			when {
				input.isBlank()        -> _displayEmptyContentWarning.value = true
				isBookNameInUse(input) -> _displayDuplicateNameWarning.value = true
				else                   -> {
					val book = Book(input)
					if(bookId != 0)
						book.id = bookId
					bookRepository.addBook(book)
					_dismiss.value = true
				}
			}
		}
	}

	private suspend fun isBookNameInUse(name: String): Boolean {
		return bookRepository.isBookNameInUse(name)
	}

	private fun getBookName() {
		viewModelScope.launch {
			_bookTitle.value = bookRepository.getBookName(bookId)
		}
	}

	fun deleteBook() {
		viewModelScope.launch {
			if(bookId != 0)
				bookRepository.deleteBook(bookId)
			_dismiss.value = true
		}
	}
}
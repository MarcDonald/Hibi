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
package com.marcdonald.hibi.screens.entryscreens.addentryscreen.addentrytobookdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Book
import com.marcdonald.hibi.data.entity.BookEntryRelation
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.BookRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddEntryToBookViewModel(bookRepository: BookRepository, private val bookEntryRelationRepository: BookEntryRelationRepository) :
		ViewModel() {

	private var entryId = 0

	val allBooks: LiveData<List<Book>> = bookRepository.getAllBooksLD()

	private var _dismiss = MutableLiveData<Boolean>()
	val dismiss: LiveData<Boolean>
		get() = _dismiss

	private var _bookEntryRelations = MutableLiveData<List<Int>>()
	val bookEntryRelations: LiveData<List<Int>>
		get() = _bookEntryRelations

	private var _displayNoBookWarning = MutableLiveData<Boolean>()
	val displayNoBookWarning: LiveData<Boolean>
		get() = _displayNoBookWarning

	fun passArguments(entryIdArg: Int) {
		entryId = entryIdArg
		viewModelScope.launch {
			if(entryId != 0)
				_bookEntryRelations.value = bookEntryRelationRepository.getBookIdsWithEntry(entryId)
			else
				Timber.e("Log: passArguments: entryId = 0")
		}
	}

	fun listReceived(isEmpty: Boolean) {
		_displayNoBookWarning.value = isEmpty
	}

	fun onSaveClick(list: ArrayList<Pair<Int, Boolean>>) {
		list.forEach { idCheckedPair ->
			// First is the BookId, Second is whether it's checked
			if(idCheckedPair.second)
				save(idCheckedPair.first)
			else
				delete(idCheckedPair.first)
		}
		_dismiss.value = true
	}

	private fun save(bookId: Int) {
		viewModelScope.launch {
			if(entryId != 0) {
				val bookEntryRelation = BookEntryRelation(bookId, entryId)
				bookEntryRelationRepository.addBookEntryRelation(bookEntryRelation)
			} else
				Timber.e("Log: save: entryId = 0")
		}
	}

	private fun delete(bookId: Int) {
		viewModelScope.launch {
			if(entryId != 0) {
				val bookEntryRelation = BookEntryRelation(bookId, entryId)
				bookEntryRelationRepository.deleteBookEntryRelation(bookEntryRelation)
			} else
				Timber.e("Log: delete: entryId = 0")
		}
	}
}
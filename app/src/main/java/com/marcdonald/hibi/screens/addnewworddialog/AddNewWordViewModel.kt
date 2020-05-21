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
package com.marcdonald.hibi.screens.addnewworddialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.NewWord
import com.marcdonald.hibi.data.repository.NewWordRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddNewWordViewModel(private val newWordRepository: NewWordRepository) : ViewModel() {
	private var entryId = 0
	private var newWordId = 0

	private var _word = MutableLiveData<NewWord>()
	val word: LiveData<NewWord>
		get() = _word

	private var _isEditMode = MutableLiveData<Boolean>()
	val isEditMode: LiveData<Boolean>
		get() = _isEditMode

	private var _displayEmptyInputWarning = MutableLiveData<Boolean>()
	val displayEmptyInputWarning: LiveData<Boolean>
		get() = _displayEmptyInputWarning

	private var _dismiss = MutableLiveData<Boolean>()
	val dismiss: LiveData<Boolean>
		get() = _dismiss

	var isQuickAdd: Boolean = false
		private set

	fun passArguments(entryIdArg: Int, newWordIdArg: Int, isQuickAdd: Boolean) {
		entryId = entryIdArg
		newWordId = newWordIdArg
		this.isQuickAdd = isQuickAdd

		if(newWordId != 0) {
			_isEditMode.value = true
			getNewWord()
		}
	}

	fun saveNewWord(word: String, reading: String, part: String, english: String, notes: String) {
		if(word.isBlank() && reading.isBlank()) {
			if(word.isBlank()) {
				_displayEmptyInputWarning.value = true
			}
		} else {
			viewModelScope.launch {
				val newWordToSave = NewWord(word, reading, part, english, notes, entryId)

				if(entryId != 0) {
					if(newWordId != 0) {
						newWordToSave.id = newWordId
					}
					newWordRepository.addNewWord(newWordToSave)
				} else {
					Timber.e("Log: saveNewWord: entryId = 0")
				}
				_dismiss.value = true
			}
		}
	}

	fun deleteNewWord() {
		if(newWordId == 0) {
			_dismiss.value = true
		} else {
			viewModelScope.launch {
				newWordRepository.deleteNewWord(newWordId)
				_dismiss.value = true
			}
		}
	}

	fun getSingleStringFromList(list: List<String>): String {
		val builder = StringBuilder()
		list.forEachIndexed { index, string ->
			if(index == 0)
				builder.append(string)
			else
				builder.append(", $string")
		}
		return builder.toString()
	}

	private fun getNewWord() {
		viewModelScope.launch {
			_word.value = newWordRepository.getNewWord(newWordId)
		}
	}
}
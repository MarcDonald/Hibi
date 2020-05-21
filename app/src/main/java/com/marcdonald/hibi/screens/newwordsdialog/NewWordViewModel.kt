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
package com.marcdonald.hibi.screens.newwordsdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcdonald.hibi.data.entity.NewWord
import com.marcdonald.hibi.data.repository.NewWordRepository
import timber.log.Timber

class NewWordViewModel(private val newWordRepository: NewWordRepository) : ViewModel() {
	private var _entryId = 0
	val entryId: Int
		get() = _entryId

	private val _displayAddButton = MutableLiveData<Boolean>()
	val displayAddButton: LiveData<Boolean>
		get() = _displayAddButton

	private val _allowEdits = MutableLiveData<Boolean>()
	val allowEdits: LiveData<Boolean>
		get() = _allowEdits

	private val _displayNoWords = MutableLiveData<Boolean>()
	val displayNoWords: LiveData<Boolean>
		get() = _displayNoWords

	fun passArguments(entryIdArg: Int, isEditModeArg: Boolean) {
		_entryId = entryIdArg
		_displayAddButton.value = isEditModeArg
		_allowEdits.value = isEditModeArg
	}

	fun listReceived(isEmpty: Boolean) {
		_displayNoWords.value = isEmpty
	}

	fun getNewWords(): LiveData<List<NewWord>> {
		return if(entryId != 0)
			newWordRepository.getNewWordsByEntryId(entryId)
		else {
			Timber.e("Log: getNewWords: entryId = 0")
			val emptyLD = MutableLiveData<List<NewWord>>()
			emptyLD.value = listOf()
			return emptyLD
		}
	}
}
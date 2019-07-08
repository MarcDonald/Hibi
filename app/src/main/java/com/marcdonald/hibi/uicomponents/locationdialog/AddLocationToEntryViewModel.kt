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
package com.marcdonald.hibi.uicomponents.locationdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.repository.EntryRepository
import kotlinx.coroutines.launch

class AddLocationToEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {
	private var entryId = 0

	private val _displayEmptyError = MutableLiveData<Boolean>()
	val displayEmptyError: LiveData<Boolean>
		get() = _displayEmptyError

	private val _currentLocation = MutableLiveData<String>()
	val currentLocation: LiveData<String>
		get() = _currentLocation

	private val _dismiss = MutableLiveData<Boolean>()
	val dismiss: LiveData<Boolean>
		get() = _dismiss

	fun passArgument(entryIdArg: Int) {
		viewModelScope.launch {
			entryId = entryIdArg
			val location = entryRepository.getLocation(entryId)
			_currentLocation.value = location
		}
	}

	fun save(input: String) {
		viewModelScope.launch {
			if(input.isNotBlank()) {
				entryRepository.saveLocation(entryId, input)
				_dismiss.value = true
			} else {
				_displayEmptyError.value = true
			}
		}
	}

	fun delete() {
		viewModelScope.launch {
			entryRepository.saveLocation(entryId, "")
			_dismiss.value = true
		}
	}
}
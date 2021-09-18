/*
 * Copyright 2021 Marc Donald
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
package com.marcdonald.hibi.screens.newwords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.NewWord
import com.marcdonald.hibi.data.repository.NewWordRepository
import kotlinx.coroutines.launch

class NewWordsViewModel(private val newWordsRepository: NewWordRepository)
	: ViewModel() {

	private val _newWords = MutableLiveData<List<NewWord>>()
	val newWords: LiveData<List<NewWord>>
	get() = _newWords

	init {
		viewModelScope.launch {
			_newWords.postValue(newWordsRepository.getAll())
		}
	}
}
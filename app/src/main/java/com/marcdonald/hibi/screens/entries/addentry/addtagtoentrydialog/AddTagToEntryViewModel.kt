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
package com.marcdonald.hibi.screens.entries.addentry.addtagtoentrydialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Tag
import com.marcdonald.hibi.data.entity.TagEntryRelation
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.data.repository.TagRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddTagToEntryViewModel(tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) :
		ViewModel() {

	private var entryId = 0

	val allTags: LiveData<List<Tag>> = tagRepository.getAllTagsLD()

	private var _dismiss = MutableLiveData<Boolean>()
	val dismiss: LiveData<Boolean>
		get() = _dismiss

	private var _tagEntryRelations = MutableLiveData<List<Int>>()
	val tagEntryRelations: LiveData<List<Int>>
		get() = _tagEntryRelations

	private var _displayNoTagsWarning = MutableLiveData<Boolean>()
	val displayNoTagsWarning: LiveData<Boolean>
		get() = _displayNoTagsWarning

	fun passArguments(entryIdArg: Int) {
		entryId = entryIdArg
		viewModelScope.launch {
			if(entryId != 0)
				_tagEntryRelations.value = tagEntryRelationRepository.getTagIdsWithEntry(entryId)
			else
				Timber.e("Log: passArguments: entryId = 0")
		}
	}

	fun listReceived(isEmpty: Boolean) {
		_displayNoTagsWarning.value = isEmpty
	}

	fun onSaveClick(list: ArrayList<Pair<Int, Boolean>>) {
		list.forEach { idCheckedPair ->
			// First is the TagId, Second is whether it's checked
			if(idCheckedPair.second)
				save(idCheckedPair.first)
			else
				delete(idCheckedPair.first)
		}
		_dismiss.value = true
	}

	private fun save(tagId: Int) {
		if(entryId != 0) {
			val tagEntryRelation = TagEntryRelation(tagId, entryId)
			viewModelScope.launch {
				tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
			}
		} else {
			Timber.e("Log: save: entryId = 0")
		}
	}

	private fun delete(tagId: Int) {
		if(entryId != 0) {
			val tagEntryRelation = TagEntryRelation(tagId, entryId)
			viewModelScope.launch {
				tagEntryRelationRepository.deleteTagEntryRelation(tagEntryRelation)
			}
		} else {
			Timber.e("Log: delete: entryId = 0")
		}
	}
}
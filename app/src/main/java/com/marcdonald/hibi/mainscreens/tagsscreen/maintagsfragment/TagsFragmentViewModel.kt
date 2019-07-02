package com.marcdonald.hibi.mainscreens.tagsscreen.maintagsfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository

class TagsFragmentViewModel(tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

	val tags = tagEntryRelationRepository.allTagDisplayItems

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	init {
		_displayLoading.value = true
	}

	fun listReceived(isEmpty: Boolean) {
		_displayNoResults.value = isEmpty
		_displayLoading.value = false
	}
}
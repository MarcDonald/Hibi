package com.marcdonald.hibi.mainscreens.booksscreen.mainbooksfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository

class BooksFragmentViewModel(bookEntryRelationRepository: BookEntryRelationRepository) :
		ViewModel() {

	val books = bookEntryRelationRepository.bookDisplayItems

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
		_displayLoading.value = false
		_displayNoResults.value = isEmpty
	}
}
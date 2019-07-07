package com.marcdonald.hibi.screens.booksscreen.mainbooksfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository

class BooksFragmentViewModelFactory(private val bookEntryRelationRepository: BookEntryRelationRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return BooksFragmentViewModel(bookEntryRelationRepository) as T
	}
}
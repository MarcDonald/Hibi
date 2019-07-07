package com.marcdonald.hibi.uicomponents.addentrytobookdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.BookRepository

class AddEntryToBookViewModelFactory(private val bookRepository: BookRepository, private val bookEntryRelationRepository: BookEntryRelationRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddEntryToBookViewModel(bookRepository, bookEntryRelationRepository) as T
	}
}
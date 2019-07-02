package com.marcdonald.hibi.uicomponents.addbookdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookRepository

class AddBookViewModelFactory(private val bookRepository: BookRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddBookViewModel(bookRepository) as T
	}
}
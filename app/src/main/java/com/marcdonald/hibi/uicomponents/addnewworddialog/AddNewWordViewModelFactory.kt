package com.marcdonald.hibi.uicomponents.addnewworddialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.NewWordRepository

class AddNewWordViewModelFactory(private val newWordRepository: NewWordRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddNewWordViewModel(newWordRepository) as T
	}
}
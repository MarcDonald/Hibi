package com.marcdonald.hibi.search.searchmoreinfoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchMoreInfoViewModelFactory
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return SearchMoreInfoViewModel() as T
	}
}
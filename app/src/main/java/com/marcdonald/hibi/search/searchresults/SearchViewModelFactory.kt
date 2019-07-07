package com.marcdonald.hibi.search.searchresults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.network.jisho.JishoAPIService

class SearchViewModelFactory(private val apiService: JishoAPIService)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return SearchViewModel(apiService) as T
	}
}
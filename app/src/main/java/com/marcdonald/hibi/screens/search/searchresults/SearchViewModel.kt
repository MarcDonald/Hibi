/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi.screens.search.searchresults

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.network.NoConnectivityException
import com.marcdonald.hibi.data.network.jisho.JishoAPIService
import com.marcdonald.hibi.data.network.jisho.apiresponse.Data
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException

class SearchViewModel(private val apiService: JishoAPIService) : ViewModel() {
	private var _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private var _displayNoConnection = MutableLiveData<Boolean>()
	val displayNoConnection: LiveData<Boolean>
		get() = _displayNoConnection

	private var _displayTimeout = MutableLiveData<Boolean>()
	val displayTimeout: LiveData<Boolean>
		get() = _displayTimeout

	private var _displayError = MutableLiveData<Boolean>()
	val displayError: LiveData<Boolean>
		get() = _displayError

	private var _displayNoResults = MutableLiveData<Boolean>()
	val displayNoResults: LiveData<Boolean>
		get() = _displayNoResults

	private var _searchResults = MutableLiveData<List<Data>>()
	val searchResults: LiveData<List<Data>>
		get() = _searchResults

	var entryId: Int = 0

	fun search(searchTerm: String) {
		_displayLoading.value = true
		viewModelScope.launch {
			try {
				val searchResponse = apiService.searchTerm(searchTerm)
				if(searchResponse.data.isNotEmpty()) {
					_searchResults.value = searchResponse.data
				} else {
					_displayNoResults.value = true
				}
			} catch(e: NoConnectivityException) {
				_displayNoConnection.value = true
			} catch(e: SocketTimeoutException) {
				_displayTimeout.value = true
			} catch(e: Exception) {
				_displayError.value = true
				Timber.e("Log: searchViewModel: search: ${e.message}")
			} finally {
				_displayLoading.value = false
			}
		}
	}
}
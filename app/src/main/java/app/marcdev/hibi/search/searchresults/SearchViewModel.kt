package app.marcdev.hibi.search.searchresults

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.network.jisho.JishoAPIService
import app.marcdev.hibi.data.network.jisho.apiresponse.Data
import app.marcdev.hibi.internal.NoConnectivityException
import kotlinx.coroutines.launch

class SearchViewModel(private val apiService: JishoAPIService) : ViewModel() {
  private var _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private var _displayNoConnection = MutableLiveData<Boolean>()
  val displayNoConnection: LiveData<Boolean>
    get() = _displayNoConnection

  private var _displayNoResults = MutableLiveData<Boolean>()
  val displayNoResults: LiveData<Boolean>
    get() = _displayNoResults

  private var _searchResults = MutableLiveData<List<Data>>()
  val searchResults: LiveData<List<Data>>
    get() = _searchResults

  fun search(searchTerm: String) {
    _displayLoading.value = true
    viewModelScope.launch {
      try {
        val searchResponse = apiService.searchTerm(searchTerm)
        if(searchResponse.data.isNotEmpty()) {
          _searchResults.value = searchResponse.data
          _displayLoading.value = false
        } else {
          _displayLoading.value = false
          _displayNoResults.value = true
        }
      } catch(e: NoConnectivityException) {
        _displayLoading.value = false
        _displayNoConnection.value = true
      }
    }
  }
}
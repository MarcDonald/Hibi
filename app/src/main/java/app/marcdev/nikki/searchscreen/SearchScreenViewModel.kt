package app.marcdev.nikki.searchscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nikki.data.JishoAPIService
import app.marcdev.nikki.data.apiresponse.SearchResponse
import kotlinx.coroutines.Deferred

class SearchScreenViewModel(private val apiService: JishoAPIService) : ViewModel() {

  fun searchTerm(searchTerm: String): Deferred<SearchResponse> {
    return apiService.searchWord(searchTerm)
  }
}
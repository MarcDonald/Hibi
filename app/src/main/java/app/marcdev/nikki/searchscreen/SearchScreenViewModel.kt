package app.marcdev.nikki.searchscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nikki.data.network.JishoAPIService
import app.marcdev.nikki.data.network.apiresponse.SearchResponse
import app.marcdev.nikki.internal.NoConnectivityException

class SearchScreenViewModel(private val apiService: JishoAPIService) : ViewModel() {

  suspend fun searchTerm(searchTerm: String): SearchResponse? {
    return try {
      apiService.searchTerm(searchTerm).await()
    } catch(e: NoConnectivityException) {
      null
    }
  }
}
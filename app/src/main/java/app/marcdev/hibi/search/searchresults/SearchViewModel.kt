package app.marcdev.hibi.search.searchresults

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.network.JishoAPIService
import app.marcdev.hibi.data.network.apiresponse.SearchResponse
import app.marcdev.hibi.internal.NoConnectivityException

class SearchViewModel(private val apiService: JishoAPIService) : ViewModel() {

  suspend fun searchTerm(searchTerm: String): SearchResponse? {
    return try {
      apiService.searchTerm(searchTerm).await()
    } catch(e: NoConnectivityException) {
      null
    }
  }
}
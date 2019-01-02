package app.marcdev.nikki.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.nikki.data.JishoAPIService

class SearchScreenViewModelFactory(private val apiService: JishoAPIService)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SearchScreenViewModel(apiService) as T
  }
}
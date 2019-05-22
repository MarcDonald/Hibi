package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchEntriesCriteriaDialogViewModelFactory()
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SearchEntriesCriteriaDialogViewModel() as T
  }
}
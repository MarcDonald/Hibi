package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.TagRepository

class SearchEntriesCriteriaDialogViewModelFactory(private val tagRepository: TagRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SearchEntriesCriteriaDialogViewModel(tagRepository) as T
  }
}
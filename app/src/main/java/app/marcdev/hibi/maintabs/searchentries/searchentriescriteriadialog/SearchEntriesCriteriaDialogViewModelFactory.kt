package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookRepository
import app.marcdev.hibi.data.repository.TagRepository

class SearchEntriesCriteriaDialogViewModelFactory(private val tagRepository: TagRepository, private val bookRepository: BookRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SearchEntriesCriteriaDialogViewModel(tagRepository, bookRepository) as T
  }
}
package app.marcdev.hibi.maintabs.booksfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository

class BooksFragmentViewModelFactory(private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return BooksFragmentViewModel(bookEntryRelationRepository) as T
  }
}
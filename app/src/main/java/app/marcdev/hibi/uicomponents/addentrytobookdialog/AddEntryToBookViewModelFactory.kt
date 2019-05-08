package app.marcdev.hibi.uicomponents.addentrytobookdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.BookRepository

class AddEntryToBookViewModelFactory(private val bookRepository: BookRepository, private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddEntryToBookViewModel(bookRepository, bookEntryRelationRepository) as T
  }
}
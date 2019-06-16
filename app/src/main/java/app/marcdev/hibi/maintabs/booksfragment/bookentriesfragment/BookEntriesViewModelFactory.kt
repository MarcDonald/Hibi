package app.marcdev.hibi.maintabs.booksfragment.bookentriesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.BookRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class BookEntriesViewModelFactory(private val bookRepository: BookRepository,
                                  private val bookEntryRelationRepository: BookEntryRelationRepository,
                                  private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return BookEntriesViewModel(bookRepository, bookEntryRelationRepository, tagEntryRelationRepository) as T
  }
}
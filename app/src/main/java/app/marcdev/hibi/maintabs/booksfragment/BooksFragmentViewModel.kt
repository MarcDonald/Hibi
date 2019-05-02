package app.marcdev.hibi.maintabs.booksfragment

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.internal.lazyDeferred

class BooksFragmentViewModel(private val bookEntryRelationRepository: BookEntryRelationRepository) : ViewModel() {

  val displayItems by lazyDeferred {
    return@lazyDeferred bookEntryRelationRepository.getBooksWithCount()
  }
}
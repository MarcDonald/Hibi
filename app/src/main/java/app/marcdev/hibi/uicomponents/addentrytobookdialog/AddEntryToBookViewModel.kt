package app.marcdev.hibi.uicomponents.addentrytobookdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.BookRepository
import app.marcdev.hibi.entryscreens.addentryscreen.BooksToSaveToNewEntry
import app.marcdev.hibi.internal.lazyDeferred

class AddEntryToBookViewModel(private val bookRepository: BookRepository, private val bookEntryRelationRepository: BookEntryRelationRepository) : ViewModel() {
  var entryId: Int = 0

  val allBooks by lazyDeferred {
    bookRepository.getAllBooks()
  }

  suspend fun getBooksForEntry(): List<Int> {
    return bookEntryRelationRepository.getBookIdsWithEntryNotLiveData(entryId)
  }

  suspend fun saveBookEntryRelation(bookId: Int) {
    if(entryId != 0) {
      val bookEntryRelation = BookEntryRelation(bookId, entryId)
      bookEntryRelationRepository.addBookEntryRelation(bookEntryRelation)
    } else {
      BooksToSaveToNewEntry.list.add(bookId)
    }
  }

  suspend fun deleteBookEntryRelation(bookId: Int) {
    if(entryId != 0) {
      val bookEntryRelation = BookEntryRelation(bookId, entryId)
      bookEntryRelationRepository.deleteBookEntryRelation(bookEntryRelation)
    } else {
      BooksToSaveToNewEntry.list.remove(bookId)
    }
  }
}
package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.maintabs.booksfragment.BookDisplayItem

interface BookEntryRelationRepository {

  suspend fun addBookEntryRelation(bookEntryRelation: BookEntryRelation)

  suspend fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation)

  suspend fun deleteBookEntryRelationByBookId(bookId: Int)

  suspend fun deleteBookEntryRelationByEntryId(entryId: Int)

  suspend fun getAllBookEntryRelations(): LiveData<List<BookEntryRelation>>

  suspend fun getEntriesWithBook(bookId: Int): LiveData<List<Entry>>

  suspend fun getBooksWithCount(): LiveData<List<BookDisplayItem>>
}
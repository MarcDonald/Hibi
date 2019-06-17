package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Book
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment.BookDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.BookEntryDisplayItem

interface BookEntryRelationRepository {

  suspend fun addBookEntryRelation(bookEntryRelation: BookEntryRelation)

  suspend fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation)

  suspend fun getEntriesWithBook(bookId: Int): List<Entry>

  val bookDisplayItems: LiveData<List<BookDisplayItem>>

  suspend fun getBookIdsWithEntry(entryId: Int): List<Int>

  suspend fun getBooksWithEntry(entryId: Int): List<Book>

  suspend fun getAllBookEntryRelationsWithIds(ids: List<Int>): List<BookEntryRelation>

  suspend fun getBookEntryDisplayItems(): List<BookEntryDisplayItem>

  fun getCountBooksWithEntryLD(entryId: Int): LiveData<Int>
}
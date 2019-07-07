package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.entity.Book
import com.marcdonald.hibi.data.entity.BookEntryRelation
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.screens.booksscreen.mainbooksfragment.BookDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem

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
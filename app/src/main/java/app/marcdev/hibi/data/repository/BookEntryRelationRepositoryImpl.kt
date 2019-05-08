package app.marcdev.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment.BookDisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookEntryRelationRepositoryImpl private constructor(private val dao: DAO) : BookEntryRelationRepository {

  override suspend fun addBookEntryRelation(bookEntryRelation: BookEntryRelation) {
    withContext(Dispatchers.IO) {
      try {
        Timber.d("Log: addBookEntryRelation: BookEntryRelation doesn't exist, adding new")
        dao.insertBookEntryRelation(bookEntryRelation)
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addBookEntryRelation: BookEntryRelation already exists, updating existing")
        dao.updateBookEntryRelation(bookEntryRelation)
      }
    }
  }

  override suspend fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation) {
    withContext(Dispatchers.IO) {
      dao.deleteBookEntryRelation(bookEntryRelation)
    }
  }

  override suspend fun deleteBookEntryRelationByBookId(bookId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteBookEntryRelationByBookId(bookId)
    }
  }

  override suspend fun deleteBookEntryRelationByEntryId(entryId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteBookEntryRelationByEntryId(entryId)
    }
  }

  override suspend fun getAllBookEntryRelations(): LiveData<List<BookEntryRelation>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllBookEntryRelations()
    }
  }

  override suspend fun getEntriesWithBook(bookId: Int): LiveData<List<Entry>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntriesWithBook(bookId)
    }
  }

  override suspend fun getBooksWithCount(): LiveData<List<BookDisplayItem>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getBooksWithCountOfEntries()
    }
  }

  override suspend fun getBookIdsWithEntryNotLiveData(entryId: Int): List<Int> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getBookIdsWithEntryNotLiveData(entryId)
    }
  }

  companion object {
    @Volatile private var instance: BookEntryRelationRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: BookEntryRelationRepositoryImpl(dao).also { instance = it }
      }
  }
}
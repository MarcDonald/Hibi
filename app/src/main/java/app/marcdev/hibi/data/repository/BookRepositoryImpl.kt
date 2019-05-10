package app.marcdev.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class BookRepositoryImpl private constructor(private val dao: DAO) : BookRepository {

  override suspend fun addBook(book: Book) {
    withContext(Dispatchers.IO) {
      try {
        Timber.d("Log: addBook: Book doesn't exist, adding new")
        dao.insertBook(book)
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addBook: Book already exists, updating existing")
        dao.updateBook(book)
      }
    }
  }

  override suspend fun deleteBook(bookId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteBook(bookId)
    }
  }

  override suspend fun getAllBooks(): LiveData<List<Book>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllBooks()
    }
  }

  override suspend fun getBookById(id: Int): Book {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getBookById(id)
    }
  }

  override suspend fun isBookNameInUse(name: String): Boolean {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getCountBooksWithName(name) > 0
    }
  }

  override suspend fun getBookName(bookId: Int): String {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getBookName(bookId)
    }
  }

  override val allBooks: LiveData<List<Book>> by lazy<LiveData<List<Book>>>(LazyThreadSafetyMode.NONE) {
    Transformations.map(dao.getAllBooks()) { it }
  }

  override suspend fun getCountOfBooks(): Int {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getCountOfBooks()
    }
  }

  companion object {
    @Volatile private var instance: BookRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: BookRepositoryImpl(dao).also { instance = it }
      }
  }
}
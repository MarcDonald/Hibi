package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Book

interface BookRepository {

  suspend fun addBook(book: Book)

  suspend fun deleteBook(bookId: Int)

  suspend fun getAllBooks(): LiveData<List<Book>>

  suspend fun getBookById(id: Int): Book

  suspend fun isBookNameInUse(name: String): Boolean

  suspend fun getBookName(bookId: Int): String

  val allBooks: LiveData<List<Book>>

  suspend fun getCountOfBooks(): Int
}
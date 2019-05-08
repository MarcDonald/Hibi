package app.marcdev.hibi.uicomponents.addbookdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Book
import app.marcdev.hibi.data.repository.BookRepository

class AddBookViewModel(private val bookRepository: BookRepository) : ViewModel() {
  var bookId: Int = 0

  private suspend fun isBookNameInUse(name: String): Boolean {
    return bookRepository.isBookNameInUse(name)
  }

  suspend fun addBook(name: String): Boolean {
    return if(name.isNotBlank() && !isBookNameInUse(name)) {
      val book = Book(name)

      if(bookId != 0)
        book.id = bookId

      bookRepository.addBook(book)
      true
    } else {
      false
    }
  }

  suspend fun getBookName(): String? {
    return bookRepository.getBookName(bookId)
  }

  suspend fun deleteBook() {
    if(bookId != 0) {
      bookRepository.deleteBook(bookId)
    }
  }
}
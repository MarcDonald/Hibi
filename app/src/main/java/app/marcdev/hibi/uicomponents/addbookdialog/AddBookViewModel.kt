package app.marcdev.hibi.uicomponents.addbookdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Book
import app.marcdev.hibi.data.repository.BookRepository
import kotlinx.coroutines.launch

class AddBookViewModel(private val bookRepository: BookRepository) : ViewModel() {
  private var _bookId = 0
  val bookId: Int
    get() = _bookId

  private val _bookTitle = MutableLiveData<String>()
  val bookTitle: LiveData<String>
    get() = _bookTitle

  private val _isEditMode = MutableLiveData<Boolean>()
  val isEditMode: LiveData<Boolean>
    get() = _isEditMode

  private val _displayEmptyContentWarning = MutableLiveData<Boolean>()
  val displayEmptyContentWarning: LiveData<Boolean>
    get() = _displayEmptyContentWarning

  private val _displayDuplicateNameWarning = MutableLiveData<Boolean>()
  val displayDuplicateNameWarning: LiveData<Boolean>
    get() = _displayDuplicateNameWarning

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss


  fun passArguments(bookIdArg: Int) {
    _bookId = bookIdArg
    if(bookId != 0) {
      _isEditMode.value = true
      getBookName()
    }
  }

  fun saveBook(input: String) {
    viewModelScope.launch {
      when {
        input.isBlank() -> _displayEmptyContentWarning.value = true
        isBookNameInUse(input) -> _displayDuplicateNameWarning.value = true
        else -> {
          val book = Book(input)
          if(bookId != 0)
            book.id = bookId
          bookRepository.addBook(book)
          _dismiss.value = true
        }
      }
    }
  }

  private suspend fun isBookNameInUse(name: String): Boolean {
    return bookRepository.isBookNameInUse(name)
  }

  private fun getBookName() {
    viewModelScope.launch {
      _bookTitle.value = bookRepository.getBookName(bookId)
    }
  }

  fun deleteBook() {
    viewModelScope.launch {
      if(bookId != 0)
        bookRepository.deleteBook(bookId)
      _dismiss.value = true
    }
  }
}
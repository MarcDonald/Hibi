package com.marcdonald.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcdonald.hibi.data.entity.Book
import com.marcdonald.hibi.data.repository.BookRepository

class AddMultiEntryToBookViewModel(bookRepository: BookRepository) : ViewModel() {
  val allBooks: LiveData<List<Book>> = bookRepository.getAllBooksLD()

  private var _displayNoBooksWarning = MutableLiveData<Boolean>()
  val displayNoBooksWarning: LiveData<Boolean>
    get() = _displayNoBooksWarning

  fun listReceived(isEmpty: Boolean) {
    _displayNoBooksWarning.value = isEmpty
  }
}
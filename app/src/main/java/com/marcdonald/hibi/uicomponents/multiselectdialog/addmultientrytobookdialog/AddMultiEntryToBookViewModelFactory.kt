package com.marcdonald.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookRepository

class AddMultiEntryToBookViewModelFactory(private val bookRepository: BookRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddMultiEntryToBookViewModel(bookRepository) as T
  }
}
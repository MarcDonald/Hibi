package com.marcdonald.hibi.uicomponents.newwordsdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.NewWordRepository

class NewWordViewModelFactory(private val newWordRepository: NewWordRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return NewWordViewModel(newWordRepository) as T
  }
}
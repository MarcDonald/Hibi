package app.marcdev.hibi.newwordsdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.NewWordRepository

class NewWordViewModelFactory(private val newWordRepository: NewWordRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return NewWordViewModel(newWordRepository) as T
  }
}
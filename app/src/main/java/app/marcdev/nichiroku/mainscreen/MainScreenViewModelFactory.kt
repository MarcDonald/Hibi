package app.marcdev.nichiroku.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.nichiroku.data.repository.EntryRepository

class MainScreenViewModelFactory(private val entryRepository: EntryRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainScreenViewModel(entryRepository) as T
  }
}
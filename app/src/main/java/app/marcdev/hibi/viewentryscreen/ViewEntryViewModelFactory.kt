package app.marcdev.hibi.viewentryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.EntryRepository

class ViewEntryViewModelFactory(private val entryRepository: EntryRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewEntryViewModel(entryRepository) as T
  }
}
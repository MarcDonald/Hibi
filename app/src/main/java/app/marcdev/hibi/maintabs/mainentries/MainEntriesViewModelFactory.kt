package app.marcdev.hibi.maintabs.mainentries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class MainEntriesViewModelFactory(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainEntriesViewModel(entryRepository, tagEntryRelationRepository) as T
  }
}
package app.marcdev.hibi.maintabs.tagsfragment.taggedentriesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class TaggedEntriesViewModelFactory(private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return TaggedEntriesViewModel(tagEntryRelationRepository) as T
  }
}
package app.marcdev.hibi.maintabs.tagsfragment.taggedentriesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.data.repository.TagRepository

class TaggedEntriesViewModelFactory(private val tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return TaggedEntriesViewModel(tagRepository, tagEntryRelationRepository) as T
  }
}
package app.marcdev.hibi.entryscreens.viewentryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class ViewEntryViewModelFactory(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository, private val newWordRepository: NewWordRepository, private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewEntryViewModel(entryRepository, tagEntryRelationRepository, newWordRepository, bookEntryRelationRepository) as T
  }
}
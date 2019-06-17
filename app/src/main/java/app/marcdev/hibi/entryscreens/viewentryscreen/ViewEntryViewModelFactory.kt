package app.marcdev.hibi.entryscreens.viewentryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.*
import app.marcdev.hibi.internal.utils.FileUtils

class ViewEntryViewModelFactory(private val entryRepository: EntryRepository,
                                private val tagEntryRelationRepository: TagEntryRelationRepository,
                                private val newWordRepository: NewWordRepository,
                                private val bookEntryRelationRepository: BookEntryRelationRepository,
                                private val entryImageRepository: EntryImageRepository,
                                private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewEntryViewModel(entryRepository, tagEntryRelationRepository, newWordRepository, bookEntryRelationRepository, entryImageRepository, fileUtils) as T
  }
}
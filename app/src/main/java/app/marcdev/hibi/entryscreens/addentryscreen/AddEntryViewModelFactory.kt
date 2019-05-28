package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class AddEntryViewModelFactory(
  private val entryRepository: EntryRepository,
  private val tagEntryRelationRepository: TagEntryRelationRepository,
  private val bookEntryRelationRepository: BookEntryRelationRepository,
  private val newWordRepository: NewWordRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddEntryViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository, newWordRepository) as T
  }
}
package app.marcdev.hibi.maintabs.searchentries.searchentriesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.*

class SearchEntriesViewModelFactory(
  private val entryRepository: EntryRepository,
  private val tagRepository: TagRepository,
  private val tagEntryRelationRepository: TagEntryRelationRepository,
  private val bookRepository: BookRepository,
  private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return SearchEntriesViewModel(entryRepository, tagRepository, tagEntryRelationRepository, bookRepository, bookEntryRelationRepository) as T
  }
}
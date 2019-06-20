package app.marcdev.hibi.mainscreens.calendarfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class CalendarTabViewModelFactory(private val entryRepository: EntryRepository,
                                  private val tagEntryRelationRepository: TagEntryRelationRepository,
                                  private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return CalendarTabViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository) as T
  }
}
package app.marcdev.nichiroku.mainscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nichiroku.data.repository.EntryRepository
import app.marcdev.nichiroku.internal.lazyDeferred

class MainScreenViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  val allEntries by lazyDeferred {
    entryRepository.getAllEntries()
  }
}
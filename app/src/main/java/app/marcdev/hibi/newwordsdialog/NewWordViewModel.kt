package app.marcdev.hibi.newwordsdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.internal.lazyDeferred

class NewWordViewModel(private val newWordRepository: NewWordRepository) : ViewModel() {
  var entryId: Int = 0

  val newWords by lazyDeferred {
    return@lazyDeferred newWordRepository.getNewWordsByEntryId(entryId)
  }
}
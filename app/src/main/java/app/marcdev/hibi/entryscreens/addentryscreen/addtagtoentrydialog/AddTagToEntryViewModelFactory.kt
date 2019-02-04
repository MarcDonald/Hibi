package app.marcdev.hibi.entryscreens.addentryscreen.addtagtoentrydialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.data.repository.TagRepository

class AddTagToEntryViewModelFactory(private val tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddTagToEntryViewModel(tagRepository, tagEntryRelationRepository) as T
  }
}
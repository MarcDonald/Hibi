package app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.internal.lazyDeferred

class TagsFragmentViewModel(private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  val displayItems by lazyDeferred {
    return@lazyDeferred tagEntryRelationRepository.getTagsWithCount()
  }
}
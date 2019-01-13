package app.marcdev.hibi.addentryscreen.addtagtoentrydialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.data.repository.TagRepository
import app.marcdev.hibi.internal.lazyDeferred

class AddTagToEntryViewModel(private val tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {
  var entryId: Int = 0

  val allTags by lazyDeferred {
    tagRepository.getAllTags()
  }

  suspend fun getTagsForEntry(): List<String> {
    return tagEntryRelationRepository.getTagsWithEntryNotLiveData(entryId)
  }

  suspend fun saveTagEntryRelation(tag: String) {
    if(entryId != 0) {
      val tagEntryRelation = TagEntryRelation(tag, entryId)
      tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
    } else {
      // TODO save temporarily and then save on full save
    }
  }

  suspend fun deleteTagEntryRelation(tag: String) {
    if(entryId != 0) {
      tagEntryRelationRepository.deleteTagEntryRelation(tag, entryId)
    } else {
      // TODO
    }
  }
}
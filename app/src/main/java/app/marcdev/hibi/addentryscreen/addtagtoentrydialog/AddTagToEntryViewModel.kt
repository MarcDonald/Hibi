package app.marcdev.hibi.addentryscreen.addtagtoentrydialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.addentryscreen.TagsToSaveToNewEntry
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
      TagsToSaveToNewEntry.list.add(tag)
    }
  }

  suspend fun deleteTagEntryRelation(tag: String) {
    if(entryId != 0) {
      val tagEntryRelation = TagEntryRelation(tag, entryId)
      tagEntryRelationRepository.deleteTagEntryRelation(tagEntryRelation)
    } else {
      TagsToSaveToNewEntry.list.remove(tag)
    }
  }
}
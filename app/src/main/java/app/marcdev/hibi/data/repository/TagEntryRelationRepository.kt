package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment.TagDisplayItem

interface TagEntryRelationRepository {

  suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun getAllTagEntryRelations(): LiveData<List<TagEntryRelation>>

  suspend fun getEntriesWithTag(tag: String): LiveData<List<Entry>>

  suspend fun getTagsWithEntry(entryId: Int): LiveData<List<String>>

  suspend fun getTagsWithEntryNotLiveData(entryId: Int): List<String>

  suspend fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun deleteTagEntryRelationByTagId(tag: String)

  suspend fun deleteTagEntryRelationByEntryId(entryId: Int)

  suspend fun getTagsWithCount(): LiveData<List<TagDisplayItem>>
}
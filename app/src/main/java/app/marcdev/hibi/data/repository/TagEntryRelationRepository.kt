package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment.TagDisplayItem

interface TagEntryRelationRepository {

  suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun getAllTagEntryRelations(): LiveData<List<TagEntryRelation>>

  suspend fun getEntriesWithTag(tagId: Int): LiveData<List<Entry>>

  suspend fun getTagsWithEntry(entryId: Int): LiveData<List<Tag>>

  suspend fun getTagsWithEntryNotLiveData(entryId: Int): List<Tag>

  suspend fun getTagIdsWithEntryNotLiveData(entryId: Int): List<Int>

  suspend fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun deleteTagEntryRelationByTagId(tagId: Int)

  suspend fun deleteTagEntryRelationByEntryId(entryId: Int)

  suspend fun getTagsWithCount(): LiveData<List<TagDisplayItem>>

  suspend fun getTagsWithCountNonLiveData(): List<TagDisplayItem>

  suspend fun getTagEntryDisplayItems(): LiveData<List<TagEntryDisplayItem>>

  suspend fun getTagEntryDisplayItemsNonLiveData(): List<TagEntryDisplayItem>

}
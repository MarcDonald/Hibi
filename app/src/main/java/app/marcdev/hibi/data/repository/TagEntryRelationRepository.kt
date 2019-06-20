package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.mainscreens.mainentriesrecycler.TagEntryDisplayItem
import app.marcdev.hibi.mainscreens.tagsfragment.maintagsfragment.TagDisplayItem

interface TagEntryRelationRepository {

  suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun getAllTagEntryRelationsWithIds(ids: List<Int>): List<TagEntryRelation>

  suspend fun getEntriesWithTag(tagId: Int): List<Entry>

  suspend fun getTagsWithEntry(entryId: Int): List<Tag>

  suspend fun getTagIdsWithEntry(entryId: Int): List<Int>

  suspend fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

  val allTagDisplayItems: LiveData<List<TagDisplayItem>>

  suspend fun getTagEntryDisplayItems(): List<TagEntryDisplayItem>

  fun getCountTagsWithEntry(entryId: Int): LiveData<Int>
}
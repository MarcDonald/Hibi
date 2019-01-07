package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation

interface TagEntryRelationRepository {

  suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation)

  suspend fun getEntriesWithTag(tagId: Int): LiveData<List<Entry>>

  suspend fun getTagsWithEntry(entryId: Int): LiveData<List<Tag>>

  suspend fun deleteTagEntryRelation(tagId: Int, entryId: Int)
}
package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.entity.Tag
import com.marcdonald.hibi.data.entity.TagEntryRelation
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem
import com.marcdonald.hibi.screens.tagsscreen.maintagsfragment.TagDisplayItem

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
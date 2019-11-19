/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.entity.Tag
import com.marcdonald.hibi.data.entity.TagEntryRelation
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem
import com.marcdonald.hibi.screens.tags.maintags.TagDisplayItem

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

	val taggedEntriesCount: LiveData<Int>

	suspend fun getTagWithMostEntries(): Tag?
}
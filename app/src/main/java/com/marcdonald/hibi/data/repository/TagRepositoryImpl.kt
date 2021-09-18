/*
 * Copyright 2021 Marc Donald
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

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TagRepositoryImpl private constructor(private val dao: DAO) : TagRepository {

	override suspend fun getTag(tagId: Int): Tag {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getTagById(tagId)
		}
	}

	override suspend fun addTag(tag: Tag) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertTag(tag)
				Timber.d("Log: addTag: Tag doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addTag: Tag already exists, updating existing")
				dao.updateTag(tag)
			}
		}
	}

	override suspend fun deleteTag(tagId: Int) {
		withContext(Dispatchers.IO) {
			dao.deleteTag(tagId)
		}
	}

	override suspend fun isTagInUse(tag: String): Boolean {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getCountTagsWithName(tag) > 0
		}
	}

	override suspend fun getTagName(tagId: Int): String {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getTagName(tagId)
		}
	}

	override fun getAllTagsLD(): LiveData<List<Tag>> {
		return dao.getAllTagsLD()
	}

	override suspend fun getAllTags(): List<Tag> {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getAllTags()
		}
	}

	companion object {
		@Volatile private var instance: TagRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: TagRepositoryImpl(dao).also { instance = it }
			}
	}
}
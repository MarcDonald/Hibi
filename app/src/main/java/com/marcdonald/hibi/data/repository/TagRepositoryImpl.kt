package com.marcdonald.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TagRepositoryImpl private constructor(private val dao: DAO) : TagRepository {

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
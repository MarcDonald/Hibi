package com.marcdonald.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.Entry
import com.marcdonald.hibi.data.entity.Tag
import com.marcdonald.hibi.data.entity.TagEntryRelation
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.TagEntryDisplayItem
import com.marcdonald.hibi.mainscreens.tagsscreen.maintagsfragment.TagDisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TagEntryRelationRepositoryImpl private constructor(private val dao: DAO) : TagEntryRelationRepository {

  override suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      try {
        dao.insertTagEntryRelation(tagEntryRelation)
        Timber.d("Log: addTagEntryRelation: TagEntryRelation doesn't exist, added new")
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addTagEntryRelation: TagEntryRelation already exists, updating existing")
        dao.updateTagEntryRelation(tagEntryRelation)
      }
    }
  }

  override suspend fun getAllTagEntryRelationsWithIds(ids: List<Int>): List<TagEntryRelation> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllTagEntryRelationsWithIds(ids)
    }
  }

  override suspend fun getEntriesWithTag(tagId: Int): List<Entry> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntriesWithTag(tagId)
    }
  }

  override suspend fun getTagsWithEntry(entryId: Int): List<Tag> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntry(entryId)
    }
  }

  override suspend fun getTagIdsWithEntry(entryId: Int): List<Int> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagIdsWithEntry(entryId)
    }
  }

  override suspend fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelation(tagEntryRelation)
    }
  }

  override val allTagDisplayItems: LiveData<List<TagDisplayItem>> by lazy {
    dao.getTagDisplayItems()
  }

  override suspend fun getTagEntryDisplayItems(): List<TagEntryDisplayItem> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagEntryDisplayItems()
    }
  }

  override fun getCountTagsWithEntry(entryId: Int): LiveData<Int> {
    return dao.getTagCountByEntryId(entryId)
  }

  companion object {
    @Volatile private var instance: TagEntryRelationRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: TagEntryRelationRepositoryImpl(dao).also { instance = it }
      }
  }
}
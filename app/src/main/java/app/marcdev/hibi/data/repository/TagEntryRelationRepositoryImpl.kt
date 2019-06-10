package app.marcdev.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment.TagDisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TagEntryRelationRepositoryImpl private constructor(private val dao: DAO) : TagEntryRelationRepository {

  override suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      try {
        Timber.d("Log: addTagEntryRelation: TagEntryRelation doesn't exist, adding new")
        dao.insertTagEntryRelation(tagEntryRelation)
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
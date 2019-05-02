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

  override suspend fun getAllTagEntryRelations(): LiveData<List<TagEntryRelation>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllTagEntryRelations()
    }
  }

  override suspend fun getEntriesWithTag(tagId: Int): LiveData<List<Entry>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntriesWithTag(tagId)
    }
  }

  override suspend fun getTagsWithEntry(entryId: Int): LiveData<List<Tag>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntry(entryId)
    }
  }

  override suspend fun getTagsWithEntryNotLiveData(entryId: Int): List<Tag> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntryNotLiveData(entryId)
    }
  }

  override suspend fun getTagIdsWithEntryNotLiveData(entryId: Int): List<Int> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagIdsWithEntryNotLiveData(entryId)
    }
  }

  override suspend fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelation(tagEntryRelation)
    }
  }

  override suspend fun deleteTagEntryRelationByEntryId(entryId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelationByEntryId(entryId)
    }
  }

  override suspend fun deleteTagEntryRelationByTagId(tagId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelationByTagId(tagId)
    }
  }

  override suspend fun getTagsWithCount(): LiveData<List<TagDisplayItem>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithCountOfEntries()
    }
  }

  override suspend fun getTagEntryDisplayItems(): LiveData<List<TagEntryDisplayItem>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagEntryDisplayItems()
    }
  }

  companion object {
    @Volatile private var instance: TagEntryRelationRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: TagEntryRelationRepositoryImpl(dao).also { instance = it }
      }
  }
}
package app.marcdev.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
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

  override suspend fun getEntriesWithTag(tag: String): LiveData<List<Entry>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntriesWithTag(tag)
    }
  }

  override suspend fun getTagsWithEntry(entryId: Int): LiveData<List<String>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntry(entryId)
    }
  }

  override suspend fun getTagsWithEntryNotLiveData(entryId: Int): List<String> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntryNotLiveData(entryId)
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

  override suspend fun deleteTagEntryRelationByTagId(tag: String) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelationByTagId(tag)
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
package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagEntryRelationRepositoryImpl private constructor(private val dao: DAO) : TagEntryRelationRepository {

  override suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      dao.upsertTagEntryRelation(tagEntryRelation)
    }
  }

  override suspend fun getEntriesWithTag(tag: String): LiveData<List<Entry>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntriesWithTag(tag)
    }
  }

  override suspend fun getTagsWithEntry(entryId: Int): List<String> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagsWithEntry(entryId)
    }
  }

  override suspend fun deleteTagEntryRelation(tag: String, entryId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelation(tag, entryId)
    }
  }

  companion object {
    @Volatile private var instance: TagEntryRelationRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: TagEntryRelationRepositoryImpl(dao).also { instance = it }
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
}
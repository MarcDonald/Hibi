package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagEntryRelationRepositoryImpl private constructor(private val dao: DAO) : TagEntryRelationRepository {

  override suspend fun addTagEntryRelation(tagEntryRelation: TagEntryRelation) {
    withContext(Dispatchers.IO) {
      dao.upsertTagEntryRelation(tagEntryRelation)
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

  override suspend fun deleteTagEntryRelation(tagId: Int, entryId: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteTagEntryRelation(tagId, entryId)
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
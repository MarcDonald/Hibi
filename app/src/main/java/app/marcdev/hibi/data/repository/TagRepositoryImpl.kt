package app.marcdev.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class TagRepositoryImpl private constructor(private val dao: DAO) : TagRepository {

  override suspend fun addTag(tag: Tag) {
    withContext(Dispatchers.IO) {
      try {
        Timber.d("Log: addTag: Tag doesn't exist, adding new")
        dao.insertTag(tag)
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addTag: Tag already exists, updating existing")
        dao.updateTag(tag)
      }
    }
  }

  override suspend fun getTagByName(tag: String): LiveData<Tag> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagByName(tag)
    }
  }

  override suspend fun getTagById(id: Int): Tag {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTagById(id)
    }
  }

  override suspend fun getAllTags(): LiveData<List<Tag>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllTags()
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

  override val allTags: LiveData<List<Tag>>by lazy {
    dao.getAllTags()
  }

  override suspend fun getCountOfTags(): Int {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getCountOfTags()
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
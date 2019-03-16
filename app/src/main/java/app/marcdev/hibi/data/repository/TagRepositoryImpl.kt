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

  override suspend fun getTag(tag: String): LiveData<Tag> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getTag(tag)
    }
  }

  override suspend fun getAllTags(): LiveData<List<Tag>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllTags()
    }
  }

  override suspend fun deleteTag(tag: String) {
    withContext(Dispatchers.IO) {
      dao.deleteTag(tag)
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
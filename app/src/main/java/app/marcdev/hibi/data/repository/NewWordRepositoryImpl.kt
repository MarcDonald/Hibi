package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.NewWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewWordRepositoryImpl private constructor(private val dao: DAO) : NewWordRepository {

  override suspend fun addNewWord(newWord: NewWord) {
    withContext(Dispatchers.IO) {
      dao.upsertNewWord(newWord)
    }
  }

  override suspend fun getNewWord(id: Int): NewWord {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getNewWord(id)
    }
  }

  override suspend fun deleteNewWord(id: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteNewWord(id)
    }
  }

  override suspend fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getNewWordsByEntryId(entryId)
    }
  }

  companion object {
    @Volatile private var instance: NewWordRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: NewWordRepositoryImpl(dao).also { instance = it }
      }
  }
}
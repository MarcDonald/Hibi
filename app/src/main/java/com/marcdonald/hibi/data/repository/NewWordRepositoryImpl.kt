package com.marcdonald.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.NewWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewWordRepositoryImpl private constructor(private val dao: DAO) : NewWordRepository {

  override suspend fun addNewWord(newWord: NewWord) {
    withContext(Dispatchers.IO) {
      try {
        dao.insertNewWord(newWord)
        Timber.d("Log: addNewWord: NewWord doesn't exist, added new")
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addNewWord: NewWord already exists, updating existing")
        dao.updateNewWord(newWord)
      }
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

  override fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>> {
    return dao.getNewWordsByEntryId(entryId)
  }

  override suspend fun getNewWordCountByEntryId(entryId: Int): Int {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getNewWordCountByEntryId(entryId)
    }
  }

  override fun getNewWordCountByEntryIdLD(entryId: Int): LiveData<Int> {
    return dao.getNewWordCountByEntryIdLD(entryId)
  }

  companion object {
    @Volatile private var instance: NewWordRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: NewWordRepositoryImpl(dao).also { instance = it }
      }
  }
}
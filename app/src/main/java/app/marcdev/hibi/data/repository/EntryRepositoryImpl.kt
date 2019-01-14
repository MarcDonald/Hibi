package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.database.DAO
import app.marcdev.hibi.data.entity.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EntryRepositoryImpl private constructor(private val dao: DAO) : EntryRepository {

  override suspend fun addEntry(entry: Entry) {
    withContext(Dispatchers.IO) {
      dao.upsertEntry(entry)
    }
  }

  override suspend fun getEntry(id: Int): LiveData<Entry> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getEntry(id)
    }
  }

  override suspend fun getAllEntries(): LiveData<List<Entry>> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAllEntries()
    }
  }

  override suspend fun deleteEntry(id: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteEntry(id)
    }
  }

  override suspend fun getAmountOfEntries(): LiveData<Int> {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getAmountOfEntries()
    }
  }

  override suspend fun getLastEntryId(): Int {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getLastEntryId()
    }
  }

  companion object {
    @Volatile private var instance: EntryRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: EntryRepositoryImpl(dao).also { instance = it }
      }
  }
}
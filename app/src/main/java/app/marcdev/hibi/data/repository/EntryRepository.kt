package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry

interface EntryRepository {

  suspend fun addEntry(entry: Entry)

  suspend fun getEntry(id: Int): LiveData<Entry>

  suspend fun getEntryNonLiveData(id: Int): Entry

  suspend fun getAllEntries(): LiveData<List<Entry>>

  suspend fun getAllEntriesNonLiveData(): List<Entry>

  suspend fun deleteEntry(id: Int)

  suspend fun getAmountOfEntries(): LiveData<Int>

  suspend fun getLastEntryId(): Int

  suspend fun getEntryCount(): LiveData<Int>

  suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): LiveData<List<Entry>>

  suspend fun getEntriesOnDateNonLiveData(year: Int, month: Int, day: Int): List<Entry>
}
package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Entry

interface EntryRepository {

  suspend fun addEntry(entry: Entry)

  suspend fun saveEntry(id: Int, day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String)

  suspend fun getEntry(id: Int): Entry

  suspend fun getAllEntries(): List<Entry>

  suspend fun deleteEntry(id: Int)

  suspend fun getLastEntryId(): Int

  suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry>

  suspend fun saveLocation(entryId: Int, location: String)

  suspend fun getLocation(entryId: Int): String

  fun getLocationLD(entryId: Int): LiveData<String>
}
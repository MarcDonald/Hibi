package app.marcdev.hibi.data.repository

import app.marcdev.hibi.data.entity.Entry

interface EntryRepository {

  suspend fun addEntry(entry: Entry)

  suspend fun getEntry(id: Int): Entry

  suspend fun getAllEntries(): List<Entry>

  suspend fun deleteEntry(id: Int)

  suspend fun getLastEntryId(): Int

  suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry>
}
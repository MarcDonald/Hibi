package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.entity.Entry
import java.util.*

interface EntryRepository {

  suspend fun addEntry(entry: Entry)

  suspend fun saveEntry(id: Int, day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String)

  suspend fun getEntry(id: Int): Entry

  suspend fun getAllEntries(): List<Entry>

  suspend fun deleteEntry(id: Int)

  suspend fun getLastEntryId(): Int

  suspend fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry>

  suspend fun getEntriesOnDate(calendar: Calendar): List<Entry>

  suspend fun getEntriesOnDate(calendar: Calendar, ascending: Boolean): List<Entry>

  suspend fun saveLocation(entryId: Int, location: String)

  suspend fun getLocation(entryId: Int): String

  fun getLocationLD(entryId: Int): LiveData<String>

  suspend fun getAllYears(): List<Int>

  suspend fun getFirstEntryOnDate(calendar: Calendar): Entry?

  suspend fun getAmountOfEntriesOnDate(calendar: Calendar): Int
}
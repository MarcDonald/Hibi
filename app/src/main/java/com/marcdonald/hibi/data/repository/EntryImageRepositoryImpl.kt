package com.marcdonald.hibi.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.database.DAO
import com.marcdonald.hibi.data.entity.EntryImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class EntryImageRepositoryImpl(private val dao: DAO) : EntryImageRepository {
	override suspend fun addEntryImage(entryImage: EntryImage) {
		withContext(Dispatchers.IO) {
			try {
				dao.insertEntryImage(entryImage)
				Timber.d("Log: addEntryImage: EntryImage doesn't exist, added new")
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addEntryImage: EntryImage already exists, updating existing")
				dao.updateEntryImage(entryImage)
			}
		}
	}

	override suspend fun deleteEntryImage(entryImage: EntryImage) {
		withContext(Dispatchers.IO) {
			dao.deleteEntryImage(entryImage)
		}
	}

	override fun getImagesForEntry(entryId: Int): LiveData<List<EntryImage>> {
		return dao.getImagesForEntryLD(entryId)
	}

	override suspend fun countUsesOfImage(imageName: String): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.countUsesOfImage(imageName)
		}
	}

	override fun getCountImagesForEntry(entryId: Int): LiveData<Int> {
		return dao.getCountImagesForEntry(entryId)
	}

	companion object {
		@Volatile private var instance: EntryImageRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: EntryImageRepositoryImpl(dao).also { instance = it }
			}
	}
}
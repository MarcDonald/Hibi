package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.NewWord

interface NewWordRepository {

  suspend fun addNewWord(newWord: NewWord)

  suspend fun getNewWord(id: Int): NewWord

  suspend fun deleteNewWord(id: Int)

  fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>>

  suspend fun getNewWordCountByEntryId(entryId: Int): Int

  fun getNewWordCountByEntryIdLD(entryId: Int): LiveData<Int>

}
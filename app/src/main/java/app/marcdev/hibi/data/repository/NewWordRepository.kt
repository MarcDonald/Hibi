package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.NewWord

interface NewWordRepository {

  suspend fun addNewWord(newWord: NewWord)

  suspend fun deleteNewWordByEntryId(entryId: Int)

  suspend fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>>
}
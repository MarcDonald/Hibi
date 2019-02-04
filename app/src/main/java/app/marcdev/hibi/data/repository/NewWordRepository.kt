package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.NewWord

interface NewWordRepository {

  suspend fun addNewWord(newWord: NewWord)

  suspend fun getNewWord(id: Int): NewWord

  suspend fun deleteNewWord(id: Int)

  suspend fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>>
}
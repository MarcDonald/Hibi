package app.marcdev.hibi.uicomponents.addnewworddialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.NewWord
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.entryscreens.addentryscreen.NewWordsToSaveToNewEntry

class AddNewWordViewModel(private val newWordRepository: NewWordRepository) : ViewModel() {
  var entryId: Int = 0
  var newWordId: Int = 0

  suspend fun saveNewWord(word: String, reading: String, part: String, english: String, notes: String) {
    val newWordToSave = NewWord(word, reading, part, english, notes, entryId)

    if(entryId != 0) {
      if(newWordId != 0) {
        newWordToSave.id = newWordId
      }
      newWordRepository.addNewWord(newWordToSave)
    } else {
      NewWordsToSaveToNewEntry.addNewWordToList(newWordToSave)
    }
  }

  suspend fun getNewWord(): NewWord {
    return newWordRepository.getNewWord(newWordId)
  }

  suspend fun deleteNewWord() {
    newWordRepository.deleteNewWord(newWordId)
  }
}
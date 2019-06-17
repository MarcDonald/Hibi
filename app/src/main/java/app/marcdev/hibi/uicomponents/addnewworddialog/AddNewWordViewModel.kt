package app.marcdev.hibi.uicomponents.addnewworddialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.NewWord
import app.marcdev.hibi.data.repository.NewWordRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddNewWordViewModel(private val newWordRepository: NewWordRepository) : ViewModel() {
  private var entryId = 0
  private var newWordId = 0

  private var _word = MutableLiveData<NewWord>()
  val word: LiveData<NewWord>
    get() = _word

  private var _isEditMode = MutableLiveData<Boolean>()
  val isEditMode: LiveData<Boolean>
    get() = _isEditMode

  private var _displayEmptyInputWarning = MutableLiveData<Boolean>()
  val displayEmptyInputWarning: LiveData<Boolean>
    get() = _displayEmptyInputWarning

  private var _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  fun passArguments(entryIdArg: Int, newWordIdArg: Int) {
    entryId = entryIdArg
    newWordId = newWordIdArg

    if(newWordId != 0) {
      _isEditMode.value = true
      getNewWord()
    }
  }

  fun saveNewWord(word: String, reading: String, part: String, english: String, notes: String) {
    if(word.isBlank() && reading.isBlank()) {
      if(word.isBlank()) {
        _displayEmptyInputWarning.value = true
      }
    } else {
      viewModelScope.launch {
        val newWordToSave = NewWord(word, reading, part, english, notes, entryId)

        if(entryId != 0) {
          if(newWordId != 0) {
            newWordToSave.id = newWordId
          }
          newWordRepository.addNewWord(newWordToSave)
        } else {
          Timber.e("Log: saveNewWord: entryId = 0")
        }
        _dismiss.value = true
      }
    }
  }

  fun deleteNewWord() {
    if(newWordId == 0) {
      _dismiss.value = true
    } else {
      viewModelScope.launch {
        newWordRepository.deleteNewWord(newWordId)
        _dismiss.value = true
      }
    }
  }

  private fun getNewWord() {
    viewModelScope.launch {
      _word.value = newWordRepository.getNewWord(newWordId)
    }
  }
}
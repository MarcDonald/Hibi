package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.marcdev.hibi.data.entity.NewWord

object NewWordsToSaveToNewEntry {
  private var newWordList = ArrayList<NewWord>()

  private val _list = MutableLiveData<List<NewWord>>()
  val list: LiveData<List<NewWord>>
    get() = _list

  fun addNewWordToList(newWord: NewWord) {
    newWordList.add(newWord)
    _list.value = newWordList.toList()
  }

  fun clearList() {
    newWordList.clear()
    _list.value = newWordList.toList()
  }
}
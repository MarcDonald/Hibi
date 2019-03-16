package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.MutableLiveData
import app.marcdev.hibi.data.entity.NewWord

object NewWordsToSaveToNewEntry {
  private var newWordList = ArrayList<NewWord>()
  val list = MutableLiveData<ArrayList<NewWord>>()

  fun addNewWordToList(newWord: NewWord) {
    newWordList.add(newWord)
    list.value = newWordList
  }

  fun clearList() {
    newWordList.clear()
    list.value = newWordList
  }
}
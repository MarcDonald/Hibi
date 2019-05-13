package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import kotlinx.coroutines.launch

class AddEntryViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository, private val bookEntryRelationRepository: BookEntryRelationRepository, private val newWordRepository: NewWordRepository) : ViewModel() {
  val dateTimeStore = DateTimeStore()

  private var _entryId = 0
  val entryId: Int
    get() = _entryId

  private var _isEditMode = MutableLiveData<Boolean>()
  val isEditMode: LiveData<Boolean>
    get() = _isEditMode

  private val _displayEmptyContentWarning = MutableLiveData<Boolean>()
  val displayEmptyContentWarning: LiveData<Boolean>
    get() = _displayEmptyContentWarning

  private val _popBackStack = MutableLiveData<Boolean>()
  val popBackStack: LiveData<Boolean>
    get() = _popBackStack

  private val _entry = MutableLiveData<Entry>()
  val entry: LiveData<Entry>
    get() = _entry

  init {
    BooksToSaveToNewEntry.list.clear()
    TagsToSaveToNewEntry.list.clear()
    NewWordsToSaveToNewEntry.clearList()
  }

  fun save(content: String) {
    viewModelScope.launch {
      if(content.isBlank()) {
        _displayEmptyContentWarning.value = true
      } else {
        val day = dateTimeStore.getDay()
        val month = dateTimeStore.getMonth()
        val year = dateTimeStore.getYear()
        val hour = dateTimeStore.getHour()
        val minute = dateTimeStore.getMinute()

        if(entryId == 0) {
          addEntry(day, month, year, hour, minute, content)
        } else {
          updateEntry(day, month, year, hour, minute, content, entryId)
        }

        _popBackStack.value = true
      }
    }
  }

  fun passArgument(entryIdArg: Int) {
    _entryId = entryIdArg
    _isEditMode.value = entryId != 0
    if(entryId != 0) {
      viewModelScope.launch {
        getEntry(entryId)
      }
    }
  }

  private suspend fun addEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String) {
    entryRepository.addEntry(Entry(day, month, year, hour, minute, content))
    val entryId = entryRepository.getLastEntryId()
    TagsToSaveToNewEntry.list.forEach { tagId ->
      val tagEntryRelation = TagEntryRelation(tagId, entryId)
      tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
    }
    BooksToSaveToNewEntry.list.forEach { bookId ->
      val bookEntryRelation = BookEntryRelation(bookId, entryId)
      bookEntryRelationRepository.addBookEntryRelation(bookEntryRelation)
    }
    if(NewWordsToSaveToNewEntry.list.value != null) {
      NewWordsToSaveToNewEntry.list.value!!.forEach { word ->
        word.entryId = entryId
        newWordRepository.addNewWord(word)
      }
    }
  }

  private suspend fun getEntry(id: Int) {
    val entry = entryRepository.getEntry(id)
    _entry.value = entry
    dateTimeStore.setDate(entry.day, entry.month, entry.year)
    dateTimeStore.setTime(entry.hour, entry.minute)
  }

  private suspend fun updateEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String, entryId: Int) {
    val entryToUpdate = Entry(day, month, year, hour, minute, content)
    entryToUpdate.id = entryId
    entryRepository.addEntry(entryToUpdate)
  }
}
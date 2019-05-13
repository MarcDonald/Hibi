package app.marcdev.hibi.entryscreens.viewentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewEntryViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository, private val newWordRepository: NewWordRepository) : ViewModel() {
  private var _entryId = 0
  val entryId: Int
    get() = _entryId

  private var _displayErrorToast = MutableLiveData<Boolean>()
  val displayErrorToast: LiveData<Boolean>
    get() = _displayErrorToast

  private var _content = MutableLiveData<String>()
  val content: LiveData<String>
    get() = _content

  private var _readableDate = MutableLiveData<String>()
  val readableDate: LiveData<String>
    get() = _readableDate

  private var _readableTime = MutableLiveData<String>()
  val readableTime: LiveData<String>
    get() = _readableTime

  private var _tags = MutableLiveData<List<Tag>>()
  val tags: LiveData<List<Tag>>
    get() = _tags

  private var _displayNewWordButton = MutableLiveData<Boolean>()
  val displayNewWordButton: LiveData<Boolean>
    get() = _displayNewWordButton

  private val _popBackStack = MutableLiveData<Boolean>()
  val popBackStack: LiveData<Boolean>
    get() = _popBackStack


  fun passArguments(entryIdArg: Int) {
    _entryId = entryIdArg
    if(entryIdArg == 0) {
      _displayErrorToast.value = true
      Timber.e("Log: passArguments: entryId is 0")
    } else {
      viewModelScope.launch {
        getEntry()
        getTags()
        getNewWords()
      }
    }
  }

  private suspend fun getEntry() {
    val entry = entryRepository.getEntry(entryId)
    _content.value = entry.content
    _readableDate.value = formatDateForDisplay(entry.day, entry.month, entry.year)
    _readableTime.value = formatTimeForDisplay(entry.hour, entry.minute)
  }

  private suspend fun getTags() {
    _tags.value = tagEntryRelationRepository.getTagsWithEntry(entryId)
  }

  private suspend fun getNewWords() {
    _displayNewWordButton.value = newWordRepository.getNewWordCountByEntryId(entryId) > 0
  }

  fun deleteEntry() {
    viewModelScope.launch {
      entryRepository.deleteEntry(entryId)
      _popBackStack.value = true
    }
  }
}
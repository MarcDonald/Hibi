package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {
  val dateTimeStore = DateTimeStore()
  private var isNewEntry: Boolean = false
  private var needsArgs = true

  private var _entryId = 0
  val entryId: Int
    get() = _entryId

  private var _isEditMode = MutableLiveData<Boolean>()
  val isEditMode: LiveData<Boolean>
    get() = _isEditMode

  private val _displayEmptyContentWarning = MutableLiveData<Boolean>()
  val displayEmptyContentWarning: LiveData<Boolean>
    get() = _displayEmptyContentWarning

  private val _displayBackWarning = MutableLiveData<Boolean>()
  val displayBackWarning: LiveData<Boolean>
    get() = _displayBackWarning

  private val _popBackStack = MutableLiveData<Boolean>()
  val popBackStack: LiveData<Boolean>
    get() = _popBackStack

  private val _entry = MutableLiveData<Entry>()
  val entry: LiveData<Entry>
    get() = _entry

  fun passArgument(entryIdArg: Int) {
    if(needsArgs) {
      _entryId = entryIdArg
      _isEditMode.value = entryId != 0
      loadData()
      needsArgs = false
    }
  }

  fun savePress(content: String) {
    if(content.isBlank()) {
      _displayEmptyContentWarning.value = true
    } else {
      save(content)
    }
    _popBackStack.value = true
  }

  fun backPress(contentIsEmpty: Boolean) {
    viewModelScope.launch {
      // If it's a new entry and there's no content, delete it, otherwise confirm the user wants to exit
      if(contentIsEmpty && isNewEntry) {
        deleteEntry()
        _popBackStack.value = true
      } else {
        _displayBackWarning.value = true
      }
    }
  }

  fun confirmBack() {
    viewModelScope.launch {
      _displayBackWarning.value = false
      // If it's a new entry, delete it, otherwise just exit without saving
      if(isNewEntry) {
        deleteEntry()
      }
      _popBackStack.value = true
    }
  }

  fun pause(content: String) {
    if(content.isNotBlank()) {
      // Saves user input when app is put into the background, in case it is killed by the OS
      Timber.w("Log: pause: onPause called, saving so user input isn't lost")
      save(content)
    }
  }

  private fun loadData() {
    if(entryId != 0) {
      getEntry(entryId)
    } else {
      isNewEntry = true
      initialAdd()
    }
  }

  private fun getEntry(id: Int) {
    viewModelScope.launch {
      val entry = entryRepository.getEntry(id)
      _entry.value = entry
      dateTimeStore.setDate(entry.day, entry.month, entry.year)
      dateTimeStore.setTime(entry.hour, entry.minute)
    }
  }

  private fun initialAdd() {
    viewModelScope.launch {
      val day = dateTimeStore.day
      val month = dateTimeStore.month
      val year = dateTimeStore.year
      val hour = dateTimeStore.hour
      val minute = dateTimeStore.minute
      entryRepository.addEntry(Entry(day, month, year, hour, minute, ""))
      _entryId = entryRepository.getLastEntryId()
    }
  }

  private fun save(content: String) {
    viewModelScope.launch {
      val day = dateTimeStore.day
      val month = dateTimeStore.month
      val year = dateTimeStore.year
      val hour = dateTimeStore.hour
      val minute = dateTimeStore.minute
      entryRepository.saveEntry(entryId, day, month, year, hour, minute, content)
    }
  }

  private suspend fun deleteEntry() {
    entryRepository.deleteEntry(entryId)
  }
}
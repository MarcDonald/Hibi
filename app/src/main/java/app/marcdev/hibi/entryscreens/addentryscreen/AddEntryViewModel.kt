package app.marcdev.hibi.entryscreens.addentryscreen

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.*
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.EntryImage
import app.marcdev.hibi.data.repository.*
import app.marcdev.hibi.internal.PREF_SAVE_ON_PAUSE
import app.marcdev.hibi.internal.utils.FileUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AddEntryViewModel(
  application: Application,
  private val entryRepository: EntryRepository,
  private val tagEntryRelationRepository: TagEntryRelationRepository,
  private val bookEntryRelationRepository: BookEntryRelationRepository,
  private val newWordRepository: NewWordRepository,
  private val entryImageRepository: EntryImageRepository,
  private val fileUtils: FileUtils)
  : AndroidViewModel(application) {

  val dateTimeStore = DateTimeStore()
  private var isNewEntry: Boolean = false
  private var needsArgs = true

  private val _entryId = MutableLiveData<Int>()
  val entryId: Int
    get() = getEntryIdNonNull()

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

  val colorTagIcon: LiveData<Boolean>
    get() = Transformations.switchMap(tagEntryRelationRepository.getCountTagsWithEntry(entryId), ::greaterThanZero)

  val colorBookIcon: LiveData<Boolean>
    get() = Transformations.switchMap(bookEntryRelationRepository.getCountBooksWithEntryLD(entryId), ::greaterThanZero)

  val colorLocationIcon: LiveData<Boolean>
    get() = Transformations.switchMap(entryRepository.getLocationLD(entryId), ::isNotBlank)

  val colorNewWordIcon: LiveData<Boolean>
    get() = Transformations.switchMap(newWordRepository.getNewWordCountByEntryIdLD(entryId), ::greaterThanZero)

  private val _startObservingEntrySpecificItems = MutableLiveData<Boolean>()
  val startObservingEntrySpecificItems: LiveData<Boolean>
    get() = _startObservingEntrySpecificItems

  fun passArgument(entryIdArg: Int) {
    if(needsArgs) {
      loadData(entryIdArg)
      needsArgs = false
    }
  }

  fun savePress(content: String) {
    if(content.isBlank()) {
      _displayEmptyContentWarning.value = true
    } else {
      save(content)
      _popBackStack.value = true
    }
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
    if(PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean(PREF_SAVE_ON_PAUSE, false)) {
      if(content.isNotBlank()) {
        // Saves user input when app is put into the background, in case it is killed by the OS
        Timber.i("Log: pause: onPause called, saving so user input isn't lost")
        save(content)
      }
    }
  }

  private fun loadData(entryIdArg: Int) {
    if(entryIdArg != 0)
      getEntry(entryIdArg)
    else
      initialAdd()
  }

  private fun getEntry(id: Int) {
    viewModelScope.launch {
      val entry = entryRepository.getEntry(id)
      _entry.value = entry
      dateTimeStore.setDate(entry.day, entry.month, entry.year)
      dateTimeStore.setTime(entry.hour, entry.minute)
      _isEditMode.value = true
      _entryId.value = id
      _startObservingEntrySpecificItems.value = true
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
      _entryId.value = entryRepository.getLastEntryId()
      _isEditMode.value = false
      isNewEntry = true
      _startObservingEntrySpecificItems.value = true
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

  private fun getEntryIdNonNull(): Int {
    return _entryId.value ?: 0
  }

  private fun greaterThanZero(amount: Int): LiveData<Boolean> {
    val ld = MutableLiveData<Boolean>()
    ld.value = amount > 0
    return ld
  }

  private fun isNotBlank(string: String): LiveData<Boolean> {
    val ld = MutableLiveData<Boolean>()
    ld.value = string.isNotBlank()
    return ld
  }

  fun addImages(pathList: List<String>) {
    viewModelScope.launch {
      pathList.forEach { path ->
        val file = File(path)
        val entryImage = EntryImage(file.name, entryId)
        entryImageRepository.addEntryImage(entryImage)
        fileUtils.saveImage(file)
      }
    }
  }
}
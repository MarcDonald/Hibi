package app.marcdev.hibi.mainscreens.entryscreens.viewentryscreen

import androidx.lifecycle.*
import app.marcdev.hibi.data.entity.Book
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.*
import app.marcdev.hibi.internal.utils.FileUtils
import app.marcdev.hibi.internal.utils.formatDateForDisplay
import app.marcdev.hibi.internal.utils.formatTimeForDisplay
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewEntryViewModel(private val entryRepository: EntryRepository,
                         private val tagEntryRelationRepository: TagEntryRelationRepository,
                         private val newWordRepository: NewWordRepository,
                         private val bookEntryRelationRepository: BookEntryRelationRepository,
                         private val entryImageRepository: EntryImageRepository,
                         private val fileUtils: FileUtils)
  : ViewModel() {
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

  private var _books = MutableLiveData<List<Book>>()
  val books: LiveData<List<Book>>
    get() = _books

  private var _displayNewWordButton = MutableLiveData<Boolean>()
  val displayNewWordButton: LiveData<Boolean>
    get() = _displayNewWordButton

  private val _popBackStack = MutableLiveData<Boolean>()
  val popBackStack: LiveData<Boolean>
    get() = _popBackStack

  private var _location = MutableLiveData<String>()
  val location: LiveData<String>
    get() = _location

  val images: LiveData<List<String>>
    get() = Transformations.switchMap(entryImageRepository.getImagesForEntry(entryId)) { list ->
      val returnList = mutableListOf<String>()
      list.forEach { entryImage ->
        returnList.add(fileUtils.imagesDirectory + entryImage.imageName)
      }
      return@switchMap MutableLiveData<List<String>>(returnList) as LiveData<List<String>>
    }

  fun passArguments(entryIdArg: Int) {
    _entryId = entryIdArg
    if(entryIdArg == 0) {
      _displayErrorToast.value = true
      Timber.e("Log: passArguments: entryId is 0")
    } else {
      getEntry()
      getTags()
      getNewWords()
      getBooks()
    }
  }

  private fun getEntry() {
    viewModelScope.launch {
      val entry = entryRepository.getEntry(entryId)
      _content.value = entry.content
      _readableDate.value = formatDateForDisplay(entry.day, entry.month, entry.year)
      _readableTime.value = formatTimeForDisplay(entry.hour, entry.minute)
      _location.value = entry.location
    }
  }

  private fun getTags() {
    viewModelScope.launch {
      _tags.value = tagEntryRelationRepository.getTagsWithEntry(entryId)
    }
  }

  private fun getNewWords() {
    viewModelScope.launch {
      _displayNewWordButton.value = newWordRepository.getNewWordCountByEntryId(entryId) > 0
    }
  }

  private fun getBooks() {
    viewModelScope.launch {
      _books.value = bookEntryRelationRepository.getBooksWithEntry(entryId)
    }
  }

  fun deleteEntry() {
    viewModelScope.launch {
      entryRepository.deleteEntry(entryId)
      _popBackStack.value = true
    }
  }
}
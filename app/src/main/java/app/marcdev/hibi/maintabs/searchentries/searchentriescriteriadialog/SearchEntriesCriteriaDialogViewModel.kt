package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Book
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.BookRepository
import app.marcdev.hibi.data.repository.TagRepository
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.maintabs.searchentries.EntrySearchCriteria
import app.marcdev.hibi.maintabs.searchentries.SearchCriteriaChangeListener
import kotlinx.coroutines.launch

class SearchEntriesCriteriaDialogViewModel(private val tagRepository: TagRepository, private val bookRepository: BookRepository) : ViewModel() {
  private var _listener: SearchCriteriaChangeListener? = null
  private val _criteria = EntrySearchCriteria()

  private val _beginningDisplay = MutableLiveData<String>()
  val beginningDisplay: LiveData<String>
    get() = _beginningDisplay

  private val _endDisplay = MutableLiveData<String>()
  val endDisplay: LiveData<String>
    get() = _endDisplay

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  private val _tags = MutableLiveData<List<Tag>>()
  val tags: LiveData<List<Tag>>
    get() = _tags

  private val _displayNoTagsWarning = MutableLiveData<Boolean>()
  val displayNoTagsWarning: LiveData<Boolean>
    get() = _displayNoTagsWarning

  private val _books = MutableLiveData<List<Book>>()
  val books: LiveData<List<Book>>
    get() = _books

  private val _displayNoBooksWarning = MutableLiveData<Boolean>()
  val displayNoBooksWarning: LiveData<Boolean>
    get() = _displayNoBooksWarning

  fun setCriteriaChangeListener(criteriaChangeListener: SearchCriteriaChangeListener) {
    _listener = criteriaChangeListener
  }

  init {
    getTags()
    getBooks()
  }

  fun reset() {
    resetStartDate()
    resetEndDate()
  }

  fun setStartDate(year: Int, month: Int, day: Int) {
    _criteria.startYear = year
    _criteria.startMonth = month
    _criteria.startDay = day
    _beginningDisplay.value = formatDateForDisplay(day, month, year)
  }

  fun setEndDate(year: Int, month: Int, day: Int) {
    _criteria.endYear = year
    _criteria.endMonth = month
    _criteria.endDay = day
    _endDisplay.value = formatDateForDisplay(day, month, year)
  }

  fun search(contentArg: String, locationArg: String, tagsArg: List<Int>, booksArg: List<Int>) {
    _criteria.content = contentArg
    _criteria.location = locationArg
    _criteria.tags = tagsArg
    _criteria.books = booksArg
    _listener?.onSearchCriteriaChange(_criteria)
    _dismiss.value = true
  }

  fun resetStartDate() {
    _criteria.startYear = 0
    _criteria.startDay = 1
    _criteria.startMonth = 0
    _beginningDisplay.value = ""
  }

  fun resetEndDate() {
    _criteria.endYear = 9999
    _criteria.endDay = 31
    _criteria.endMonth = 11
    _endDisplay.value = ""
  }

  private fun getTags() {
    viewModelScope.launch {
      val allTags = tagRepository.getAllTags()
      _tags.value = allTags
      _displayNoTagsWarning.value = allTags.isEmpty()
    }
  }

  private fun getBooks() {
    viewModelScope.launch {
      val allBooks = bookRepository.getAllBooks()
      _books.value = allBooks
      _displayNoBooksWarning.value = allBooks.isEmpty()
    }
  }
}
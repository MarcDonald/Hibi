package app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import kotlinx.coroutines.launch

class BooksFragmentViewModel(private val bookEntryRelationRepository: BookEntryRelationRepository) : ViewModel() {

  private val _entries = MutableLiveData<List<BookDisplayItem>>()
  val entries: LiveData<List<BookDisplayItem>>
    get() = _entries

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displayNoResults = MutableLiveData<Boolean>()
  val displayNoResults: LiveData<Boolean>
    get() = _displayNoResults

  fun loadData() {
    viewModelScope.launch {
      _displayLoading.value = true
      _entries.value = bookEntryRelationRepository.getBooksWithCountNonLiveData()
      _displayLoading.value = false
      _displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
    }
  }
}
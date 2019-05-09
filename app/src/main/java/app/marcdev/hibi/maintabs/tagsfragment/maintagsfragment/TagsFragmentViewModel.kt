package app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import kotlinx.coroutines.launch

class TagsFragmentViewModel(private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  private val _entries = MutableLiveData<List<TagDisplayItem>>()
  val entries: LiveData<List<TagDisplayItem>>
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
      _displayNoResults.value = false
      _entries.value = tagEntryRelationRepository.getTagsWithCountNonLiveData()
      _displayLoading.value = false
      _displayNoResults.value = entries.value == null || entries.value!!.isEmpty()
    }
  }
}
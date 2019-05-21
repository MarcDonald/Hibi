package app.marcdev.hibi.uicomponents.locationdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.repository.EntryRepository
import kotlinx.coroutines.launch

class AddLocationToEntryViewModel(private val entryRepository: EntryRepository) : ViewModel() {
  private var entryId = 0

  private val _displayEmptyError = MutableLiveData<Boolean>()
  val displayEmptyError: LiveData<Boolean>
    get() = _displayEmptyError

  private val _currentLocation = MutableLiveData<String>()
  val currentLocation: LiveData<String>
    get() = _currentLocation

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  fun passArgument(entryIdArg: Int) {
    viewModelScope.launch {
      entryId = entryIdArg
      val location = entryRepository.getLocation(entryId)
      _currentLocation.value = location
    }
  }

  fun save(input: String) {
    viewModelScope.launch {
      if(input.isNotBlank()) {
        entryRepository.saveLocation(entryId, input)
        _dismiss.value = true
      } else {
        _displayEmptyError.value = true
      }
    }
  }

  fun delete() {
    viewModelScope.launch {
      entryRepository.saveLocation(entryId, "")
      _dismiss.value = true
    }
  }
}
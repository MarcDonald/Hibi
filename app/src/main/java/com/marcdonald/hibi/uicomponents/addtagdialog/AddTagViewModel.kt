package com.marcdonald.hibi.uicomponents.addtagdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.entity.Tag
import com.marcdonald.hibi.data.repository.TagRepository
import kotlinx.coroutines.launch

class AddTagViewModel(private val tagRepository: TagRepository) : ViewModel() {
  private var _tagId = 0
  val tagId: Int
    get() = _tagId

  private val _tagName = MutableLiveData<String>()
  val tagName: LiveData<String>
    get() = _tagName

  private val _isEditMode = MutableLiveData<Boolean>()
  val isEditMode: LiveData<Boolean>
    get() = _isEditMode

  private val _displayEmptyContentWarning = MutableLiveData<Boolean>()
  val displayEmptyContentWarning: LiveData<Boolean>
    get() = _displayEmptyContentWarning

  private val _displayDuplicateNameWarning = MutableLiveData<Boolean>()
  val displayDuplicateNameWarning: LiveData<Boolean>
    get() = _displayDuplicateNameWarning

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss


  fun passArguments(tagIdArg: Int) {
    _tagId = tagIdArg
    if(tagId != 0) {
      _isEditMode.value = true
      getTagName()
    }
  }

  fun saveTag(input: String) {
    viewModelScope.launch {
      when {
        input.isBlank() -> _displayEmptyContentWarning.value = true
        isTagNameInUse(input) -> _displayDuplicateNameWarning.value = true
        else -> {
          val tag = Tag(input)
          if(tagId != 0)
            tag.id = tagId
          tagRepository.addTag(tag)
          _dismiss.value = true
        }
      }
    }
  }

  private suspend fun isTagNameInUse(name: String): Boolean {
    return tagRepository.isTagInUse(name)
  }

  private fun getTagName() {
    viewModelScope.launch {
      _tagName.value = tagRepository.getTagName(tagId)
    }
  }

  fun deleteTag() {
    viewModelScope.launch {
      if(tagId != 0)
        tagRepository.deleteTag(tagId)
      _dismiss.value = true
    }
  }
}
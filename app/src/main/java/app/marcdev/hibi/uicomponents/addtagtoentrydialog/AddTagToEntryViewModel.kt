package app.marcdev.hibi.uicomponents.addtagtoentrydialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.TagEntryRelationRepository
import app.marcdev.hibi.data.repository.TagRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AddTagToEntryViewModel(tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {
  private var entryId = 0

  val allTags: LiveData<List<Tag>> = tagRepository.getAllTagsLD()

  private var _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  private var _tagEntryRelations = MutableLiveData<List<Int>>()
  val tagEntryRelations: LiveData<List<Int>>
    get() = _tagEntryRelations

  private var _displayNoTagsWarning = MutableLiveData<Boolean>()
  val displayNoTagsWarning: LiveData<Boolean>
    get() = _displayNoTagsWarning

  fun passArguments(entryIdArg: Int) {
    entryId = entryIdArg
    viewModelScope.launch {
      if(entryId != 0)
        _tagEntryRelations.value = tagEntryRelationRepository.getTagIdsWithEntry(entryId)
      else
        Timber.e("Log: passArguments: entryId = 0")
    }
  }

  fun listReceived(isEmpty: Boolean) {
    _displayNoTagsWarning.value = isEmpty
  }

  fun onSaveClick(list: ArrayList<Pair<Int, Boolean>>) {
    list.forEach { idCheckedPair ->
      // First is the TagId, Second is whether it's checked
      if(idCheckedPair.second)
        save(idCheckedPair.first)
      else
        delete(idCheckedPair.first)
    }
    _dismiss.value = true
  }

  private fun save(tagId: Int) {
    if(entryId != 0) {
      val tagEntryRelation = TagEntryRelation(tagId, entryId)
      viewModelScope.launch {
        tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
      }
    } else {
      Timber.e("Log: save: entryId = 0")
    }
  }

  private fun delete(tagId: Int) {
    if(entryId != 0) {
      val tagEntryRelation = TagEntryRelation(tagId, entryId)
      viewModelScope.launch {
        tagEntryRelationRepository.deleteTagEntryRelation(tagEntryRelation)
      }
    } else {
      Timber.e("Log: delete: entryId = 0")
    }
  }
}
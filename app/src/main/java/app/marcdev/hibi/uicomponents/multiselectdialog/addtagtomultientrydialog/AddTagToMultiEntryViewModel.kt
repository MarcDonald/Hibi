package app.marcdev.hibi.uicomponents.multiselectdialog.addtagtomultientrydialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.TagRepository

class AddTagToMultiEntryViewModel(tagRepository: TagRepository) : ViewModel() {
  val allTags: LiveData<List<Tag>> = tagRepository.getAllTagsLD()

  private var _displayNoTagsWarning = MutableLiveData<Boolean>()
  val displayNoTagsWarning: LiveData<Boolean>
    get() = _displayNoTagsWarning

  fun listReceived(isEmpty: Boolean) {
    _displayNoTagsWarning.value = isEmpty
  }
}
package app.marcdev.hibi.uicomponents.addtagdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.TagRepository

class AddTagViewModel(private val tagRepository: TagRepository) : ViewModel() {
  var tagName: String? = null

  private suspend fun doesTagAlreadyExist(name: String): Boolean {
    return tagRepository.isTagInUse(name)
  }

  suspend fun addTag(name: String): Boolean {
    return if(name.isNotBlank() && !doesTagAlreadyExist(name)) {
      val tag = Tag(name)
      tagRepository.addTag(tag)
      true
    } else {
      false
    }
  }

  suspend fun deleteTag() {
    if(tagName != null) {
      tagRepository.deleteTag(tagName!!)
    }
  }
}
package app.marcdev.hibi.uicomponents.addtagdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.TagRepository

class AddTagViewModel(private val tagRepository: TagRepository) : ViewModel() {
  var tagId: Int = 0

  private suspend fun doesTagAlreadyExist(name: String): Boolean {
    return tagRepository.isTagInUse(name)
  }

  suspend fun addTag(name: String): Boolean {
    return if(name.isNotBlank() && !doesTagAlreadyExist(name)) {
      val tag = Tag(name)

      if(tagId != 0)
        tag.id = tagId

      tagRepository.addTag(tag)
      true
    } else {
      false
    }
  }

  suspend fun getTagName(): String? {
    return tagRepository.getTagName(tagId)
  }

  suspend fun deleteTag() {
    if(tagId != 0) {
      tagRepository.deleteTag(tagId)
    }
  }
}
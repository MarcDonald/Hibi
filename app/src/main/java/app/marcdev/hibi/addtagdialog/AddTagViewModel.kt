package app.marcdev.hibi.addtagdialog

import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.repository.TagRepository

class AddTagViewModel(private val tagRepository: TagRepository) : ViewModel() {

  suspend fun addTag(name: String): Boolean {
    return if(name.isNotBlank()) {
      val tag = Tag(name)
      tagRepository.addTag(tag)
      true
    } else {
      false
    }
  }
}
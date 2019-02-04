package app.marcdev.hibi.addtagdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.repository.TagRepository

class AddTagViewModelFactory(private val tagRepository: TagRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddTagViewModel(tagRepository) as T
  }
}
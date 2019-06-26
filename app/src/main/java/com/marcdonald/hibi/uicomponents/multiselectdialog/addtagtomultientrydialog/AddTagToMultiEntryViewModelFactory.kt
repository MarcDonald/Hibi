package com.marcdonald.hibi.uicomponents.multiselectdialog.addtagtomultientrydialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.TagRepository

class AddTagToMultiEntryViewModelFactory(private val tagRepository: TagRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddTagToMultiEntryViewModel(tagRepository) as T
  }
}
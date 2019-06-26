package com.marcdonald.hibi.mainscreens.tagsscreen.maintagsfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository

class TagsFragmentViewModelFactory(private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return TagsFragmentViewModel(tagEntryRelationRepository) as T
  }
}
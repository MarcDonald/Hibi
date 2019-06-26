package com.marcdonald.hibi.mainscreens.tagsscreen.taggedentriesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.data.repository.TagRepository

class TaggedEntriesViewModelFactory(private val tagRepository: TagRepository,
                                    private val tagEntryRelationRepository: TagEntryRelationRepository,
                                    private val bookEntryRelationRepository: BookEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return TaggedEntriesViewModel(tagRepository, tagEntryRelationRepository, bookEntryRelationRepository) as T
  }
}
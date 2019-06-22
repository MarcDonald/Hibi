package com.marcdonald.hibi.mainscreens.booksfragment.bookentriesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.BookRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository

class BookEntriesViewModelFactory(private val bookRepository: BookRepository,
                                  private val bookEntryRelationRepository: BookEntryRelationRepository,
                                  private val tagEntryRelationRepository: TagEntryRelationRepository)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return BookEntriesViewModel(bookRepository, bookEntryRelationRepository, tagEntryRelationRepository) as T
  }
}
package com.marcdonald.hibi.screens.calendarscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.BookEntryRelationRepository
import com.marcdonald.hibi.data.repository.EntryRepository
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository

class CalendarTabViewModelFactory(private val entryRepository: EntryRepository,
																	private val tagEntryRelationRepository: TagEntryRelationRepository,
																	private val bookEntryRelationRepository: BookEntryRelationRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return CalendarTabViewModel(entryRepository, tagEntryRelationRepository, bookEntryRelationRepository) as T
	}
}
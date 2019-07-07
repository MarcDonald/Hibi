package com.marcdonald.hibi.screens.entryscreens.addentryscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.*
import com.marcdonald.hibi.internal.utils.FileUtils

class AddEntryViewModelFactory(private val application: Application,
															 private val entryRepository: EntryRepository,
															 private val tagEntryRelationRepository: TagEntryRelationRepository,
															 private val bookEntryRelationRepository: BookEntryRelationRepository,
															 private val newWordRepository: NewWordRepository,
															 private val entryImageRepository: EntryImageRepository,
															 private val fileUtils: FileUtils)
	: ViewModelProvider.AndroidViewModelFactory(application) {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddEntryViewModel(application, entryRepository, tagEntryRelationRepository, bookEntryRelationRepository, newWordRepository, entryImageRepository, fileUtils) as T
	}
}
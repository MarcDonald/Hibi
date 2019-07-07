package com.marcdonald.hibi.uicomponents.addtagtoentrydialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.TagEntryRelationRepository
import com.marcdonald.hibi.data.repository.TagRepository

class AddTagToEntryViewModelFactory(private val tagRepository: TagRepository, private val tagEntryRelationRepository: TagEntryRelationRepository)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddTagToEntryViewModel(tagRepository, tagEntryRelationRepository) as T
	}
}
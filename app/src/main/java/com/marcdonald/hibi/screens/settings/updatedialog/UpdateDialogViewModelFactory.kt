package com.marcdonald.hibi.screens.settings.updatedialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.internal.utils.UpdateUtils

class UpdateDialogViewModelFactory(private val updateUtils: UpdateUtils)
	: ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return UpdateDialogViewModel(updateUtils) as T
	}
}
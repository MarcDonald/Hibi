package com.marcdonald.hibi.mainscreens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.internal.utils.UpdateUtils

class MainScreenViewModelFactory(private val application: Application,
																 private val updateUtils: UpdateUtils)
	: ViewModelProvider.AndroidViewModelFactory(application) {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return MainScreenViewModel(application, updateUtils) as T
	}
}
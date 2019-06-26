package com.marcdonald.hibi.mainscreens.settings.restoredialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.database.AppDatabase
import com.marcdonald.hibi.internal.utils.FileUtils

class RestoreDialogViewModelFactory(private val fileUtils: FileUtils,
                                    private val database: AppDatabase)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return RestoreDialogViewModel(fileUtils, database) as T
  }
}
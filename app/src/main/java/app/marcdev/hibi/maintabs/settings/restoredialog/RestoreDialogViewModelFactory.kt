package app.marcdev.hibi.maintabs.settings.restoredialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.data.database.AppDatabase
import app.marcdev.hibi.internal.utils.FileUtils

class RestoreDialogViewModelFactory(private val fileUtils: FileUtils, private val database: AppDatabase)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return RestoreDialogViewModel(fileUtils, database) as T
  }
}
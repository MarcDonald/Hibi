package app.marcdev.hibi.maintabs.settings.backupdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.hibi.internal.FileUtils

class BackupDialogViewModelFactory(private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return BackupDialogViewModel(fileUtils) as T
  }
}
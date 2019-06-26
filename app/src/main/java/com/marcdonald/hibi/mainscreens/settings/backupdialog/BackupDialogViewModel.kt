package com.marcdonald.hibi.mainscreens.settings.backupdialog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.internal.PRODUCTION_DATABASE_NAME
import com.marcdonald.hibi.internal.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class BackupDialogViewModel(private val fileUtils: FileUtils) : ViewModel() {
  private var _localBackupUri: Uri? = null
  val localBackupURI: Uri?
    get() = _localBackupUri

  private val _displayDismiss = MutableLiveData<Boolean>()
  val displayDismiss: LiveData<Boolean>
    get() = _displayDismiss

  private val _displayShareButton = MutableLiveData<Boolean>()
  val displayShareButton: LiveData<Boolean>
    get() = _displayShareButton

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displaySuccess = MutableLiveData<Boolean>()
  val displaySuccess: LiveData<Boolean>
    get() = _displaySuccess

  init {
    _displaySuccess.value = false
    _displayShareButton.value = false
    _displayDismiss.value = false
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.IO) {
      _displayLoading.postValue(true)
      performBackup()
      _displayLoading.postValue(false)
      _displayDismiss.postValue(true)
      _displaySuccess.postValue(true)
      _displayShareButton.postValue(true)
    }
  }

  private fun performBackup() {
    File(fileUtils.localBackupDirectory).mkdirs()
    val filePath = fileUtils.localBackupDirectory + "Backup.hibi"
    val file = File(filePath)
    if(file.exists())
      file.delete()
    file.createNewFile()

    val zipOutputStream = ZipOutputStream(BufferedOutputStream(FileOutputStream(file)))
    backupDatabase(zipOutputStream)
    backupImages(zipOutputStream)
    zipOutputStream.close()

    _localBackupUri = fileUtils.getUriForFilePath(filePath)
  }

  private fun backupDatabase(zipOutputStream: ZipOutputStream) {
    val databaseFileInputStream = FileInputStream(fileUtils.databaseDirectory)
    val databaseBufferedInputStream = BufferedInputStream(databaseFileInputStream)
    val databaseEntry = ZipEntry(PRODUCTION_DATABASE_NAME)
    zipOutputStream.putNextEntry(databaseEntry)
    databaseBufferedInputStream.copyTo(zipOutputStream, 1024)
    zipOutputStream.closeEntry()
    databaseBufferedInputStream.close()
  }

  private fun backupImages(zipOutputStream: ZipOutputStream) {
    for(fileName in File(fileUtils.imagesDirectory).list()) {
      val fileInputStream = FileInputStream(fileUtils.imagesDirectory + fileName)
      val origin = BufferedInputStream(fileInputStream)
      val entry = ZipEntry(fileName)
      zipOutputStream.putNextEntry(entry)
      origin.copyTo(zipOutputStream, 1024)
      zipOutputStream.closeEntry()
      origin.close()
    }
  }
}
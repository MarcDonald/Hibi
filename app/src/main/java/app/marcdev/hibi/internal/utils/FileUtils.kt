package app.marcdev.hibi.internal.utils

import android.net.Uri

interface FileUtils {
  fun getUriForFilePath(filePath: String): Uri

  val localBackupDirectory: String

  val databaseDirectory: String
}
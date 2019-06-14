package app.marcdev.hibi.internal

import android.net.Uri

interface FileUtils {
  fun getUriForFilePath(filePath: String): Uri

  val localBackupDirectory: String

  val databaseDirectory: String
}
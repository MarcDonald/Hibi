package app.marcdev.hibi.internal.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import app.marcdev.hibi.internal.PACKAGE
import app.marcdev.hibi.internal.PRODUCTION_DATABASE_NAME
import java.io.File

class FileUtilsImpl(private val context: Context) : FileUtils {
  override fun getUriForFilePath(filePath: String): Uri {
    val file = File(filePath)
    return FileProvider.getUriForFile(context, "$PACKAGE.FileProvider", file)
  }

  override val localBackupDirectory: String
    get() = context.filesDir.path + "/backup/"

  override val databaseDirectory: String
    get() = context.getDatabasePath(PRODUCTION_DATABASE_NAME).path

}
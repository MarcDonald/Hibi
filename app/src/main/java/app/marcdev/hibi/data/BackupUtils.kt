package app.marcdev.hibi.data

import android.content.Context
import app.marcdev.hibi.data.database.AppDatabase
import app.marcdev.hibi.internal.PRODUCTION_DATABASE_NAME
import timber.log.Timber
import java.io.File

class BackupUtils(private val database: AppDatabase) {
  fun restore(context: Context, path: String): Boolean {
    val newDB = File(path)
    if(newDB.extension != "hibi")
      return false

    if(newDB.exists()) {
      val toDB = context.getDatabasePath(PRODUCTION_DATABASE_NAME)

      if(toDB.compareTo(newDB) != 0) {
        try {
          database.closeDB()
          newDB.copyTo(toDB, true)
          return true
        } catch(e: NoSuchFileException) {
          Timber.e("Log: backup: $e")
        }
      }
    } else {
      Timber.e("Log: restore: newDB = ${newDB.exists()}")
    }
    return false
  }
}
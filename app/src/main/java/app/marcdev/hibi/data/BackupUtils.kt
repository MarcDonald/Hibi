package app.marcdev.hibi.data

import android.content.Context
import android.os.Environment
import app.marcdev.hibi.data.database.AppDatabase
import app.marcdev.hibi.internal.EXTERNAL_BACKUP_PATH
import app.marcdev.hibi.internal.INTERNAL_BACKUP_PATH
import app.marcdev.hibi.internal.PRODUCTION_DATABASE_NAME
import timber.log.Timber
import java.io.File

class BackupUtils(private val database: AppDatabase) {
  fun backup(context: Context): Boolean {
    // Not really too sure if this works, but it doesn't seem to negatively impact anything so I'm leaving it
    database.checkpoint()

    val ogDB = context.getDatabasePath(PRODUCTION_DATABASE_NAME)

    if(ogDB.exists()) {
      val toDBExternal = File(Environment.getExternalStorageDirectory().path + EXTERNAL_BACKUP_PATH + ogDB.name)
      val toDBInternal = File(context.filesDir.path + INTERNAL_BACKUP_PATH + ogDB.name)

      if(toDBExternal.compareTo(ogDB) != 0) {
        try {
          ogDB.copyTo(toDBExternal, true)
          ogDB.copyTo(toDBInternal, true)
          return true
        } catch(e: NoSuchFileException) {
          Timber.e("Log: backup: $e")
        }
      }
    } else {
      Timber.e("Log: backup: ogDB = ${ogDB.exists()}")
    }
    return false
  }

  fun restore(context: Context): Boolean {
    val newDB = File(Environment.getExternalStorageDirectory().path + EXTERNAL_BACKUP_PATH + PRODUCTION_DATABASE_NAME)

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
      Timber.e("Log: backup: ogDB = ${newDB.exists()}")
    }
    return false
  }
}
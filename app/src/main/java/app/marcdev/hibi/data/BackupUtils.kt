package app.marcdev.hibi.data

import android.content.Context
import android.os.Environment
import app.marcdev.hibi.data.database.AppDatabase
import app.marcdev.hibi.internal.BACKUP_PATH
import app.marcdev.hibi.internal.PRODUCTION_DATABASE_NAME
import timber.log.Timber
import java.io.File

class BackupUtils(private val database: AppDatabase) {
  fun backup(context: Context): Boolean {
    // Not really too sure if this works, but it doesn't seem to negatively impact anything so I'm leaving it
    database.checkpoint()

    val ogDB = context.getDatabasePath(PRODUCTION_DATABASE_NAME)
    val ogDBshm = File(ogDB.path + "-shm")
    val ogDBwal = File(ogDB.path + "-wal")

    if(ogDB.exists() && ogDBshm.exists() && ogDBwal.exists()) {
      val toDB = File(Environment.getExternalStorageDirectory().path + BACKUP_PATH + ogDB.name)
      val toSHM = File(Environment.getExternalStorageDirectory().path + BACKUP_PATH + ogDBshm.name)
      val toWAL = File(Environment.getExternalStorageDirectory().path + BACKUP_PATH + ogDBwal.name)

      if(toDB.compareTo(ogDB) != 0) {
        try {
          ogDB.copyTo(toDB, true)
          ogDBshm.copyTo(toSHM, true)
          ogDBwal.copyTo(toWAL, true)
          return true
        } catch(e: NoSuchFileException) {
          Timber.e("Log: backup: $e")
        }
      }
    } else {
      Timber.e("Log: backup: ogDB = ${ogDB.exists()}")
      Timber.e("Log: backup: ogDBshm = ${ogDBshm.exists()}")
      Timber.e("Log: backup: ogDBwal = ${ogDBwal.exists()}")
    }
    return false
  }
}
package app.marcdev.hibi.data

import android.content.Context
import app.marcdev.hibi.data.database.AppDatabase
import app.marcdev.hibi.internal.PRODUCTION_DATABASE_NAME
import timber.log.Timber
import java.io.File

class BackupUtils(private val database: AppDatabase) {
  fun backup(context: Context) {
    database.checkpoint()
    val ogDB = context.getDatabasePath(PRODUCTION_DATABASE_NAME)
    val ogDBshm = File(ogDB.path + "-shm")
    val ogDBwal = File(ogDB.path + "-wal")

    if(ogDB.exists() && ogDBshm.exists() && ogDBwal.exists()) {
      val toDB = File(context.filesDir.parent + "/backup/" + ogDB.name)
      val toSHM = File(context.filesDir.parent + "/backup/" + ogDBshm.name)
      val toWAL = File(context.filesDir.parent + "/backup/" + ogDBwal.name)
      if(toDB.compareTo(ogDB) != 0) {
        try {
          ogDB.copyTo(toDB, true)
          ogDBshm.copyTo(toSHM, true)
          ogDBwal.copyTo(toWAL, true)
        } catch(e: NoSuchFileException) {
          Timber.e("Log: backup: $e")
        }
      }
    } else {
      Timber.e("Log: backup: ogDB = ${ogDB.exists()}")
      Timber.e("Log: backup: ogDBshm = ${ogDBshm.exists()}")
      Timber.e("Log: backup: ogDBwal = ${ogDBwal.exists()}")
    }
  }
}
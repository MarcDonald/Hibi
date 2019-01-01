package app.marcdev.nikki.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.marcdev.nikki.data.entity.Entry

@Database(entities = [Entry::class], version = 3)

abstract class ProductionAppDatabase : RoomDatabase(), AppDatabase {
  abstract override fun dao(): DAO

  companion object {
    @Volatile private var instance: ProductionAppDatabase? = null
    private val LOCK = Any()

    operator fun invoke(context: Context) = instance
                                            ?: synchronized(LOCK) {
                                              instance
                                              ?: buildDatabase(context).also { instance = it }
                                            }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(context.applicationContext,
        ProductionAppDatabase::class.java,
        "ProductionAppDatabase.db")
        .fallbackToDestructiveMigration()
        .build()
  }
}
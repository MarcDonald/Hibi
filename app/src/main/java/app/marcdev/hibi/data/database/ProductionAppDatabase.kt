package app.marcdev.hibi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation

@Database(entities = [Entry::class, Tag::class, TagEntryRelation::class], version = 5)

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
        .addMigrations(MIGRATION_3_TO_5())
        .fallbackToDestructiveMigration()
        .build()

    class MIGRATION_3_TO_5 : Migration(3, 5) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE entries RENAME TO Entry")
        database.execSQL("CREATE TABLE Tag('name' TEXT NOT NULL, PRIMARY KEY (name))")
        database.execSQL("CREATE TABLE TagEntryRelation('tag' TEXT NOT NULL, 'entryId' INTEGER NOT NULL, PRIMARY KEY (tag, entryId))")
      }
    }
  }
}
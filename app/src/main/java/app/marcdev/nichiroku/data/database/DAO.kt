package app.marcdev.nichiroku.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.marcdev.nichiroku.data.entity.Entry

@Dao
interface DAO {

  /* Entry */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun upsertEntry(entry: Entry)

  @Query("SELECT * FROM entries WHERE id = :id")
  fun getEntry(id: Int): LiveData<Entry>

  @Query("SELECT * FROM entries")
  fun getAllEntries(): LiveData<List<Entry>>

  @Query("DELETE FROM entries WHERE id = :id")
  fun deleteEntry(id: Int)

  @Query("SELECT COUNT(*) FROM entries")
  fun getAmountOfEntries(): LiveData<Int>
}

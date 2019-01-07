package app.marcdev.hibi.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation

@Dao
interface DAO {

  /* Entry */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun upsertEntry(entry: Entry)

  @Query("SELECT * FROM Entry WHERE id = :id")
  fun getEntry(id: Int): LiveData<Entry>

  @Query("SELECT * FROM Entry")
  fun getAllEntries(): LiveData<List<Entry>>

  @Query("DELETE FROM Entry WHERE id = :id")
  fun deleteEntry(id: Int)

  @Query("SELECT COUNT(*) FROM Entry")
  fun getAmountOfEntries(): LiveData<Int>

  /* Tag */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun upsertTag(tag: Tag)

  @Query("SELECT * FROM Tag")
  fun getAllTags(): LiveData<List<Tag>>

  @Query("SELECT * FROM Tag WHERE id = :id")
  fun getTag(id: Int): LiveData<Tag>

  @Query("DELETE FROM Tag WHERE id = :id")
  fun deleteTag(id: Int)

  /* Tag Entry Relation */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun upsertTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Query("SELECT * FROM Entry WHERE id = (SELECT entryId FROM TagEntryRelation WHERE tagId = :tagId)")
  fun getEntriesWithTag(tagId: Int): LiveData<List<Entry>>

  @Query("SELECT * FROM Tag WHERE id = (SELECT tagId FROM TagEntryRelation WHERE entryId = :entryId)")
  fun getTagsWithEntry(entryId: Int): LiveData<List<Tag>>

  @Query("DELETE FROM TagEntryRelation WHERE tagId = :tagId AND entryId = :entryId")
  fun deleteTagEntryRelation(tagId: Int, entryId: Int)
}

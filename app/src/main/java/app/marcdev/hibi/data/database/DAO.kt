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
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun upsertTag(tag: Tag)

  @Query("SELECT * FROM Tag")
  fun getAllTags(): LiveData<List<Tag>>

  @Query("SELECT * FROM Tag WHERE name = :tag")
  fun getTag(tag: String): LiveData<Tag>

  @Query("DELETE FROM Tag WHERE name = :tag")
  fun deleteTag(tag: String)

  /* Tag Entry Relation */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun upsertTagEntryRelation(tagEntryRelation: TagEntryRelation)

  // This query may not work
  @Query("SELECT * FROM Entry WHERE id = (SELECT entryId FROM TagEntryRelation WHERE tag = :tag)")
  fun getEntriesWithTag(tag: String): LiveData<List<Entry>>

  @Query("SELECT tag FROM TagEntryRelation WHERE entryId = :entryId")
  fun getTagsWithEntry(entryId: Int): LiveData<List<String>>

  @Query("SELECT tag FROM TagEntryRelation WHERE entryId = :entryId")
  fun getTagsWithEntryNotLiveData(entryId: Int): List<String>

  @Query("DELETE FROM TagEntryRelation WHERE tag = :tag AND entryId = :entryId")
  fun deleteTagEntryRelation(tag: String, entryId: Int)

  @Query("DELETE FROM TagEntryRelation WHERE tag = :tag")
  fun deleteTagEntryRelationByTagId(tag: String)

  @Query("DELETE FROM TagEntryRelation WHERE entryId = :entryId")
  fun deleteTagEntryRelationByEntryId(entryId: Int)
}

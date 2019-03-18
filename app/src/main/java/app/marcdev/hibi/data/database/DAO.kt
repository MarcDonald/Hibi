package app.marcdev.hibi.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.NewWord
import app.marcdev.hibi.data.entity.Tag
import app.marcdev.hibi.data.entity.TagEntryRelation

@Dao
interface DAO {
  /* Entry */
  /* When a constraint violation occurs, ignores the one row that is the constraint violation and
     continues with the rest. This stops the entity being replaced which triggered a delete cascade
     for all foreign keys
     See https://sqlite.org/lang_conflict.html
   */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertEntry(entry: Entry)

  @Update
  fun updateEntry(entry: Entry)

  @Query("SELECT * FROM Entry WHERE id = :id")
  fun getEntry(id: Int): LiveData<Entry>

  @Query("SELECT * FROM Entry")
  fun getAllEntries(): LiveData<List<Entry>>

  @Query("DELETE FROM Entry WHERE id = :id")
  fun deleteEntry(id: Int)

  @Query("SELECT COUNT(*) FROM Entry")
  fun getAmountOfEntries(): LiveData<Int>

  @Query("SELECT id FROM Entry ORDER BY id DESC LIMIT 1")
  fun getLastEntryId(): Int

  @Query("SELECT COUNT(id) FROM Entry")
  fun getEntryCount(): LiveData<Int>

  /* Tag */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertTag(tag: Tag)

  @Update
  fun updateTag(tag: Tag)

  @Query("SELECT * FROM Tag")
  fun getAllTags(): LiveData<List<Tag>>

  @Query("SELECT * FROM Tag WHERE name = :tag")
  fun getTag(tag: String): LiveData<Tag>

  @Query("DELETE FROM Tag WHERE name = :tag")
  fun deleteTag(tag: String)

  /* Tag Entry Relation */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Update
  fun updateTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Query("SELECT * FROM TagEntryRelation")
  fun getAllTagEntryRelations(): LiveData<List<TagEntryRelation>>

  // This query may not work
  @Query("SELECT * FROM Entry WHERE id = (SELECT entryId FROM TagEntryRelation WHERE tag = :tag)")
  fun getEntriesWithTag(tag: String): LiveData<List<Entry>>

  @Query("SELECT tag FROM TagEntryRelation WHERE entryId = :entryId")
  fun getTagsWithEntry(entryId: Int): LiveData<List<String>>

  @Query("SELECT tag FROM TagEntryRelation WHERE entryId = :entryId")
  fun getTagsWithEntryNotLiveData(entryId: Int): List<String>

  @Delete
  fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Query("DELETE FROM TagEntryRelation WHERE tag = :tag")
  fun deleteTagEntryRelationByTagId(tag: String)

  @Query("DELETE FROM TagEntryRelation WHERE entryId = :entryId")
  fun deleteTagEntryRelationByEntryId(entryId: Int)

  /* New Word */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertNewWord(newWord: NewWord)

  @Update
  fun updateNewWord(newWord: NewWord)

  @Query("SELECT * FROM NewWord WHERE id = :id")
  fun getNewWord(id: Int): NewWord

  @Query("DELETE FROM NewWord WHERE id = :id")
  fun deleteNewWord(id: Int)

  @Query("SELECT * FROM NewWord WHERE entryId = :entryId")
  fun getNewWordsByEntryId(entryId: Int): LiveData<List<NewWord>>

  @Query("SELECT COUNT(*) FROM NewWord WHERE entryId = :entryId")
  fun getNewWordCountByEntryId(entryId: Int): Int
}

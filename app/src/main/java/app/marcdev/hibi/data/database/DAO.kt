package app.marcdev.hibi.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import app.marcdev.hibi.data.entity.*
import app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment.BookDisplayItem
import app.marcdev.hibi.maintabs.mainentriesrecycler.TagEntryDisplayItem
import app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment.TagDisplayItem

@Dao
interface DAO {
  // <editor-fold desc="Entry">
  /* When a constraint violation occurs, ignores the one row that is the constraint violation and
   * continues with the rest. This stops the entity being replaced which triggered a delete cascade
   * for all foreign keys
   * See https://sqlite.org/lang_conflict.html
   */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertEntry(entry: Entry)

  @Update
  fun updateEntry(entry: Entry)

  @Query("SELECT * FROM Entry WHERE id = :id")
  fun getEntry(id: Int): LiveData<Entry>

  @Query("SELECT * FROM Entry WHERE id = :id")
  fun getEntryNonLiveData(id: Int): Entry

  @Query("SELECT * FROM Entry ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
  fun getAllEntries(): LiveData<List<Entry>>

  @Query("SELECT * FROM Entry ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
  fun getAllEntriesNonLiveData(): List<Entry>

  @Query("DELETE FROM Entry WHERE id = :id")
  fun deleteEntry(id: Int)

  @Query("SELECT COUNT(*) FROM Entry")
  fun getAmountOfEntries(): LiveData<Int>

  @Query("SELECT id FROM Entry ORDER BY id DESC LIMIT 1")
  fun getLastEntryId(): Int

  @Query("SELECT COUNT(id) FROM Entry")
  fun getEntryCount(): LiveData<Int>

  @Query("SELECT * FROM Entry WHERE year = :year AND month = :month AND day = :day")
  fun getEntriesOnDate(year: Int, month: Int, day: Int): LiveData<List<Entry>>
  // </editor-fold>

  // <editor-fold desc="Tag">
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertTag(tag: Tag)

  @Update
  fun updateTag(tag: Tag)

  @Query("SELECT * FROM Tag")
  fun getAllTags(): LiveData<List<Tag>>

  @Query("SELECT * FROM Tag WHERE id = :id")
  fun getTagById(id: Int): Tag

  @Query("SELECT * FROM Tag WHERE name = :tag")
  fun getTagByName(tag: String): LiveData<Tag>

  @Query("DELETE FROM Tag WHERE id = :tagId")
  fun deleteTag(tagId: Int)

  @Query("SELECT COUNT(*) From Tag WHERE name = :tag")
  fun getCountTagsWithName(tag: String): Int

  @Query("SELECT name FROM Tag WHERE id = :tagId")
  fun getTagName(tagId: Int): String
  // </editor-fold>

  // <editor-fold desc="Tag Entry Relation">
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Update
  fun updateTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Query("SELECT * FROM TagEntryRelation")
  fun getAllTagEntryRelations(): LiveData<List<TagEntryRelation>>

  @Query("SELECT * FROM Entry as e INNER JOIN TagEntryRelation as ter ON e.id = ter.entryId WHERE ter.tagId = :tagId")
  fun getEntriesWithTag(tagId: Int): LiveData<List<Entry>>

  @Query("SELECT * FROM Tag INNER JOIN TagEntryRelation ON Tag.id = TagEntryRelation.tagId WHERE entryId = :entryId")
  fun getTagsWithEntry(entryId: Int): LiveData<List<Tag>>

  @Query("SELECT * FROM Tag INNER JOIN TagEntryRelation ON Tag.id = TagEntryRelation.tagId WHERE entryId = :entryId")
  fun getTagsWithEntryNotLiveData(entryId: Int): List<Tag>

  @Query("SELECT id FROM Tag INNER JOIN TagEntryRelation ON Tag.id = TagEntryRelation.tagId WHERE entryId = :entryId")
  fun getTagIdsWithEntryNotLiveData(entryId: Int): List<Int>

  @Delete
  fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

  @Query("DELETE FROM TagEntryRelation WHERE tagId= :tagId")
  fun deleteTagEntryRelationByTagId(tagId: Int)

  @Query("DELETE FROM TagEntryRelation WHERE entryId = :entryId")
  fun deleteTagEntryRelationByEntryId(entryId: Int)

  @Query("SELECT t.id as tagID, t.name as tagName, COUNT(ter.entryId) as useCount FROM Tag as t LEFT OUTER JOIN TagEntryRelation as ter ON t.id = ter.tagId GROUP BY t.name")
  fun getTagsWithCountOfEntries(): LiveData<List<TagDisplayItem>>

  @Query("SELECT ter.entryId as entryId, t.name as tagName FROM TagEntryRelation as ter LEFT OUTER JOIN Tag as t ON ter.tagId = t.id")
  fun getTagEntryDisplayItems(): LiveData<List<TagEntryDisplayItem>>

  @Query("SELECT ter.entryId as entryId, t.name as tagName FROM TagEntryRelation as ter LEFT OUTER JOIN Tag as t ON ter.tagId = t.id")
  fun getTagEntryDisplayItemsNonLiveData(): List<TagEntryDisplayItem>

  // </editor-fold>

  // <editor-fold desc="New Word">
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
  // </editor-fold>

  // <editor-fold desc="Book">
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertBook(book: Book)

  @Update
  fun updateBook(book: Book)

  @Query("SELECT * FROM Book")
  fun getAllBooks(): LiveData<List<Book>>

  @Query("SELECT * FROM Book WHERE id = :id")
  fun getBookById(id: Int): Book

  @Query("DELETE FROM Book WHERE id = :bookId")
  fun deleteBook(bookId: Int)

  @Query("SELECT COUNT(*) From Book WHERE name = :tag")
  fun getCountBooksWithName(tag: String): Int

  @Query("SELECT name FROM Book WHERE id = :bookId")
  fun getBookName(bookId: Int): String
  // </editor-fold>

  // <editor-fold desc="Book Entry Relation">
  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insertBookEntryRelation(bookEntryRelation: BookEntryRelation)

  @Update
  fun updateBookEntryRelation(bookEntryRelation: BookEntryRelation)

  @Delete
  fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation)

  @Query("DELETE FROM BookEntryRelation WHERE bookId= :bookId")
  fun deleteBookEntryRelationByBookId(bookId: Int)

  @Query("DELETE FROM BookEntryRelation WHERE entryId = :entryId")
  fun deleteBookEntryRelationByEntryId(entryId: Int)

  @Query("SELECT * FROM BookEntryRelation")
  fun getAllBookEntryRelations(): LiveData<List<BookEntryRelation>>

  @Query("SELECT * FROM Entry as e INNER JOIN BookEntryRelation as ber ON e.id = ber.entryId WHERE ber.bookId = :bookId")
  fun getEntriesWithBook(bookId: Int): LiveData<List<Entry>>

  @Query("SELECT b.id as bookId, b.name as bookName, COUNT(ber.entryId) as useCount FROM Book as b LEFT OUTER JOIN BookEntryRelation as ber ON b.id = ber.bookId GROUP BY b.name")
  fun getBooksWithCountOfEntries(): LiveData<List<BookDisplayItem>>

  @Query("SELECT id FROM Book INNER JOIN BookEntryRelation ON Book.id = BookEntryRelation.bookId WHERE entryId = :entryId")
  fun getBookIdsWithEntryNotLiveData(entryId: Int): List<Int>
  // </editor-fold>
}

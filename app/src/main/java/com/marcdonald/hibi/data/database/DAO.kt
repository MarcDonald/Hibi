/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marcdonald.hibi.data.entity.*
import com.marcdonald.hibi.screens.booksscreen.mainbooksfragment.BookDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.BookEntryDisplayItem
import com.marcdonald.hibi.screens.mainentriesrecycler.TagEntryDisplayItem
import com.marcdonald.hibi.screens.tagsscreen.maintagsfragment.TagDisplayItem

@Dao
interface DAO {

	// <editor-fold desc="Entry">
	/* Aborting causes a SQLiteConstraintException which is then caught in the repositories and an
	 * update occurs instead. This stops the entity being replaced which triggered a delete cascade
	 * for all foreign keys
	 * See https://sqlite.org/lang_conflict.html
	 */
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertEntry(entry: Entry)

	@Update
	fun updateEntry(entry: Entry)

	@Query("UPDATE Entry SET day = :day, month = :month, year = :year, hour = :hour, minute = :minute, content = :content WHERE id = :id")
	fun saveEntry(id: Int, day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String)

	@Query("SELECT * FROM Entry WHERE id = :id")
	fun getEntry(id: Int): Entry

	@Query("SELECT * FROM Entry ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
	fun getAllEntries(): List<Entry>

	@Query("DELETE FROM Entry WHERE id = :id")
	fun deleteEntry(id: Int)

	@Query("SELECT id FROM Entry ORDER BY id DESC LIMIT 1")
	fun getLastEntryId(): Int

	@Query("SELECT * FROM Entry WHERE year = :year AND month = :month AND day = :day ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
	fun getEntriesOnDate(year: Int, month: Int, day: Int): List<Entry>

	@Query("SELECT * FROM Entry WHERE year = :year AND month = :month AND day = :day ORDER BY year ASC, month ASC, day ASC, hour ASC, minute ASC, id ASC")
	fun getEntriesOnDateAscending(year: Int, month: Int, day: Int): List<Entry>

	@Query("UPDATE Entry SET location = :location WHERE id = :entryId")
	fun setLocation(entryId: Int, location: String)

	@Query("SELECT location FROM Entry WHERE id = :entryId")
	fun getLocation(entryId: Int): String

	@Query("SELECT location FROM Entry WHERE id = :entryId")
	fun getLocationLD(entryId: Int): LiveData<String>

	@Query("SELECT DISTINCT year FROM Entry ORDER BY year DESC")
	fun getAllYears(): List<Int>

	@Query("SELECT * FROM Entry WHERE year = :year AND month = :month AND day = :day ORDER BY year ASC, month ASC, day ASC, hour ASC, minute ASC, id LIMIT 1")
	fun getFirstEntryOnDate(year: Int, month: Int, day: Int): Entry

	@Query("SELECT COUNT(*) FROM Entry WHERE year = :year AND month = :month AND day = :day")
	fun getAmountOfEntriesOnDate(year: Int, month: Int, day: Int): Int
	// </editor-fold>

	// <editor-fold desc="Tag">
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertTag(tag: Tag)

	@Update
	fun updateTag(tag: Tag)

	@Query("SELECT * FROM Tag")
	fun getAllTagsLD(): LiveData<List<Tag>>

	@Query("SELECT * FROM Tag")
	fun getAllTags(): List<Tag>

	@Query("DELETE FROM Tag WHERE id = :tagId")
	fun deleteTag(tagId: Int)

	@Query("SELECT COUNT(*) From Tag WHERE name = :tag")
	fun getCountTagsWithName(tag: String): Int

	@Query("SELECT name FROM Tag WHERE id = :tagId")
	fun getTagName(tagId: Int): String
	// </editor-fold>

	// <editor-fold desc="Tag Entry Relation">
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertTagEntryRelation(tagEntryRelation: TagEntryRelation)

	@Update
	fun updateTagEntryRelation(tagEntryRelation: TagEntryRelation)

	@Query("SELECT * FROM TagEntryRelation WHERE tagId IN (:ids)")
	fun getAllTagEntryRelationsWithIds(ids: List<Int>): List<TagEntryRelation>

	@Query("SELECT * FROM Entry as e INNER JOIN TagEntryRelation as ter ON e.id = ter.entryId WHERE ter.tagId = :tagId ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
	fun getEntriesWithTag(tagId: Int): List<Entry>

	@Query("SELECT Tag.id, Tag.name FROM Tag INNER JOIN TagEntryRelation ON Tag.id = TagEntryRelation.tagId WHERE entryId = :entryId")
	fun getTagsWithEntry(entryId: Int): List<Tag>

	@Query("SELECT id FROM Tag INNER JOIN TagEntryRelation ON Tag.id = TagEntryRelation.tagId WHERE entryId = :entryId")
	fun getTagIdsWithEntry(entryId: Int): List<Int>

	@Delete
	fun deleteTagEntryRelation(tagEntryRelation: TagEntryRelation)

	@Query("SELECT t.id as tagID, t.name as tagName, COUNT(ter.entryId) as useCount FROM Tag as t LEFT OUTER JOIN TagEntryRelation as ter ON t.id = ter.tagId GROUP BY t.name")
	fun getTagDisplayItems(): LiveData<List<TagDisplayItem>>

	@Query("SELECT ter.entryId as entryId, t.name as tagName FROM TagEntryRelation as ter LEFT OUTER JOIN Tag as t ON ter.tagId = t.id")
	fun getTagEntryDisplayItems(): List<TagEntryDisplayItem>

	@Query("SELECT COUNT(*) FROM TagEntryRelation WHERE entryId = :entryId")
	fun getTagCountByEntryId(entryId: Int): LiveData<Int>

	@Query("SELECT * FROM Entry WHERE isFavourite = 1 ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
	fun getFavouriteEntries(): List<Entry>

	@Query("UPDATE Entry SET isFavourite = :isFavourite WHERE id = :id")
	fun setEntryIsFavourite(id: Int, isFavourite: Boolean)
	// </editor-fold>

	// <editor-fold desc="New Word">
	@Insert(onConflict = OnConflictStrategy.ABORT)
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

	@Query("SELECT COUNT(*) FROM NewWord WHERE entryId = :entryId")
	fun getNewWordCountByEntryIdLD(entryId: Int): LiveData<Int>
	// </editor-fold>

	// <editor-fold desc="Book">
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertBook(book: Book)

	@Update
	fun updateBook(book: Book)

	@Query("SELECT * FROM Book")
	fun getAllBooksLD(): LiveData<List<Book>>

	@Query("SELECT * FROM Book")
	fun getAllBooks(): List<Book>

	@Query("DELETE FROM Book WHERE id = :bookId")
	fun deleteBook(bookId: Int)

	@Query("SELECT COUNT(*) From Book WHERE name = :tag")
	fun getCountBooksWithName(tag: String): Int

	@Query("SELECT name FROM Book WHERE id = :bookId")
	fun getBookName(bookId: Int): String
	// </editor-fold>

	// <editor-fold desc="Book Entry Relation">
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertBookEntryRelation(bookEntryRelation: BookEntryRelation)

	@Update
	fun updateBookEntryRelation(bookEntryRelation: BookEntryRelation)

	@Delete
	fun deleteBookEntryRelation(bookEntryRelation: BookEntryRelation)

	@Query("SELECT * FROM Entry as e INNER JOIN BookEntryRelation as ber ON e.id = ber.entryId WHERE ber.bookId = :bookId ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, id DESC")
	fun getEntriesWithBook(bookId: Int): List<Entry>

	@Query("SELECT b.id as bookId, b.name as bookName, COUNT(ber.entryId) as useCount FROM Book as b LEFT OUTER JOIN BookEntryRelation as ber ON b.id = ber.bookId GROUP BY b.name")
	fun getBookDisplayItems(): LiveData<List<BookDisplayItem>>

	@Query("SELECT id FROM Book INNER JOIN BookEntryRelation ON Book.id = BookEntryRelation.bookId WHERE entryId = :entryId")
	fun getBookIdsWithEntry(entryId: Int): List<Int>

	@Query("SELECT Book.id, Book.name FROM Book INNER JOIN BookEntryRelation ON Book.id = BookEntryRelation.bookId WHERE entryId = :entryId")
	fun getBooksWithEntry(entryId: Int): List<Book>

	@Query("SELECT * FROM BookEntryRelation WHERE bookId IN (:ids)")
	fun getAllBookEntryRelationsWithIds(ids: List<Int>): List<BookEntryRelation>

	@Query("SELECT COUNT(*) FROM BookEntryRelation WHERE entryId = :entryId")
	fun getCountBooksWithEntry(entryId: Int): LiveData<Int>

	@Query("SELECT ber.entryId as entryId, b.name as bookName FROM BookEntryRelation as ber LEFT OUTER JOIN Book as b ON ber.bookId = b.id")
	fun getBookEntryDisplayItems(): List<BookEntryDisplayItem>
	// </editor-fold>

	// <editor-fold desc="Entry Image">
	@Insert(onConflict = OnConflictStrategy.ABORT)
	fun insertEntryImage(entryImage: EntryImage)

	@Update
	fun updateEntryImage(entryImage: EntryImage)

	@Delete
	fun deleteEntryImage(entryImage: EntryImage)

	@Query("SELECT * FROM EntryImage WHERE entryId = :entryId")
	fun getImagesForEntry(entryId: Int): List<EntryImage>

	@Query("SELECT * FROM EntryImage WHERE entryId = :entryId")
	fun getImagesForEntryLD(entryId: Int): LiveData<List<EntryImage>>

	@Query("SELECT COUNT(*) FROM EntryImage WHERE imageName = :imageName")
	fun countUsesOfImage(imageName: String): Int

	@Query("SELECT COUNT(*) FROM EntryImage WHERE entryId = :entryId")
	fun getCountImagesForEntry(entryId: Int): LiveData<Int>
	// </editor-fold>
}

package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "BookEntryRelation",
  primaryKeys = ["bookId", "entryId"],
  foreignKeys = [
    ForeignKey(
      entity = Entry::class,
      parentColumns = ["id"],
      childColumns = ["entryId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = Book::class,
      parentColumns = ["id"],
      childColumns = ["bookId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    )
  ],
  indices = [Index(name = "BookEntryRelation_EntryId_Index", value = ["entryId"])]
)
data class BookEntryRelation(
  var bookId: Int,
  var entryId: Int
)
package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "TagEntryRelation",
  primaryKeys = ["tagId", "entryId"],
  foreignKeys = [
    ForeignKey(
      entity = Entry::class,
      parentColumns = ["id"],
      childColumns = ["entryId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = Tag::class,
      parentColumns = ["id"],
      childColumns = ["tagId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    )
  ],
  indices = [Index(name = "TagEntryRelation_EntryId_Index", value = ["entryId"])]
)
data class TagEntryRelation(
  var tagId: Int,
  var entryId: Int
)
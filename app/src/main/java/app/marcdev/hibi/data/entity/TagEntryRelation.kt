package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "TagEntryRelation",
  primaryKeys = ["tag", "entryId"],
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
      parentColumns = ["name"],
      childColumns = ["tag"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    )
  ])
data class TagEntryRelation(
  var tag: String,
  var entryId: Int
)
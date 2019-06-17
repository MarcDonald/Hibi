package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "EntryImage",
  primaryKeys = ["imageName", "entryId"],
  foreignKeys = [
    ForeignKey(
      entity = Entry::class,
      parentColumns = ["id"],
      childColumns = ["entryId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE
    )
  ])
data class EntryImage(
  var imageName: String,
  var entryId: Int
)
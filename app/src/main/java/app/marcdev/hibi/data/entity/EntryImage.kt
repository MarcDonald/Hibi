package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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
  ],
  indices = [Index(name = "EntryImage_EntryId_Index", value = ["entryId"])]
)
data class EntryImage(
  var imageName: String,
  var entryId: Int
)
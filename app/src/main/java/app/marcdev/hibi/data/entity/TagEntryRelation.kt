package app.marcdev.hibi.data.entity

import androidx.room.Entity

@Entity(tableName = "TagEntryRelation", primaryKeys = ["tag", "entryId"])
data class TagEntryRelation(
  var tag: String,
  var entryId: Int
)
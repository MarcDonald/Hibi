package app.marcdev.hibi.data.entity

import androidx.room.Entity

@Entity(tableName = "TagEntryRelation", primaryKeys = ["tagId", "entryId"])
data class TagEntryRelation(
  var tagId: Int,
  var entryId: Int
)
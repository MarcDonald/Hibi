package app.marcdev.nichiroku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
  var date: String,
  var time: String,
  var content: String
) {

  @PrimaryKey(autoGenerate = true)
  var id: Int? = null
}
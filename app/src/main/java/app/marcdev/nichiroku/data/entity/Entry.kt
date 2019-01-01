package app.marcdev.nichiroku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
  var day: Int,
  var month: Int,
  var year: Int,
  var hour: Int,
  var minute: Int,
  var content: String
) {

  @PrimaryKey(autoGenerate = true)
  var id: Int? = null
}
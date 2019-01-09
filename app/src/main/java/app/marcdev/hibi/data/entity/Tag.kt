package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tag")
data class Tag(
  @PrimaryKey
  var name: String
)
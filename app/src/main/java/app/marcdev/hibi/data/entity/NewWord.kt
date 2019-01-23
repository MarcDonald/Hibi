package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewWord")
data class NewWord(
  var word: String,
  var reading: String,
  var partOfSpeech: String,
  var english: String,
  var notes: String,
  var entryId: Int
) {

  @PrimaryKey(autoGenerate = true)
  var id: Int = 0
}
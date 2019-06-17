package app.marcdev.hibi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "NewWord",
  foreignKeys = [
    ForeignKey(
      entity = Entry::class,
      parentColumns = ["id"],
      childColumns = ["entryId"],
      onDelete = ForeignKey.CASCADE,
      onUpdate = ForeignKey.CASCADE)
  ])
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
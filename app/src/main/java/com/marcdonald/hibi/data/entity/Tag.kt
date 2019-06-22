package com.marcdonald.hibi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tag")
data class Tag(
  var name: String
) {
  @PrimaryKey(autoGenerate = true)
  var id: Int = 0
}
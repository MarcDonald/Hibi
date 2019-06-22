package com.marcdonald.hibi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class Book(
  var name: String
) {
  @PrimaryKey(autoGenerate = true)
  var id: Int = 0
}
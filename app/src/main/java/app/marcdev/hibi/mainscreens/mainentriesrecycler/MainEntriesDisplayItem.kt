package app.marcdev.hibi.mainscreens.mainentriesrecycler

import app.marcdev.hibi.data.entity.Entry

data class MainEntriesDisplayItem(var entry: Entry, var tags: List<String>, var books: List<String>) {
  var isSelected: Boolean = false
}
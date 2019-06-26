package com.marcdonald.hibi.mainscreens.throwbackscreen

import android.preference.PreferenceManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_BOOKS
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_LOCATION
import com.marcdonald.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_TAGS
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.internal.utils.formatDateForDisplay
import com.marcdonald.hibi.internal.utils.formatTimeForDisplay
import java.util.*

class ThrowbackRecyclerViewHolder(itemView: View)
  : RecyclerView.ViewHolder(itemView) {

  // <editor-fold desc="UI Components">
  private var titleDisplay: TextView = itemView.findViewById(R.id.txt_throwback_title)
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var tagDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_tags)
  private var bookDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_books)
  private var locationDisplay: TextView = itemView.findViewById(R.id.txt_item_location)
  private var moreEntriesDisplay: TextView = itemView.findViewById(R.id.txt_throwback_more_entries)
  // </editor-fold>

  // <editor-fold desc="Other">
  private var displayedItem: ThrowbackDisplayItem? = null
  private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)
  // </editor-fold>

  fun display(item: ThrowbackDisplayItem) {
    this.displayedItem = item
    val dateDisplayText = formatDateForDisplay(item.entryDisplayItem.entry.day, item.entryDisplayItem.entry.month, item.entryDisplayItem.entry.year)
    dateDisplay.text = dateDisplayText
    val timeDisplayText = formatTimeForDisplay(item.entryDisplayItem.entry.hour, item.entryDisplayItem.entry.minute)
    timeDisplay.text = timeDisplayText
    contentDisplay.text = item.entryDisplayItem.entry.content
    displayTitle()
    displayLocation()
    displayTags()
    displayBooks()
    if(item.amountOfOtherEntries > 0) {
      moreEntriesDisplay.text = itemView.resources.getQuantityString(R.plurals.throwback_more_entries, item.amountOfOtherEntries, item.amountOfOtherEntries)
      moreEntriesDisplay.show(true)
    } else {
      moreEntriesDisplay.show(false)
    }
  }

  private fun displayTitle() {
    displayedItem?.let { item ->
      val calendar = Calendar.getInstance()
      calendar.set(Calendar.DAY_OF_MONTH, 1)
      calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
      if(item.entryDisplayItem.entry.month == calendar.get(Calendar.MONTH)) {
        titleDisplay.text = itemView.resources.getString(R.string.last_month)
      } else {
        titleDisplay.text = item.entryDisplayItem.entry.year.toString()
      }
    }
  }

  private fun displayLocation() {
    if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_LOCATION, true)) {
      displayedItem?.let { item ->
        if(item.entryDisplayItem.entry.location.isNotBlank()) {
          locationDisplay.text = item.entryDisplayItem.entry.location
          locationDisplay.show(true)
        } else {
          locationDisplay.show(false)
        }
      }
    } else {
      locationDisplay.show(false)
    }
  }

  private fun displayTags() {
    if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_TAGS, true)) {
      displayedItem?.let { item ->
        tagDisplay.show(item.entryDisplayItem.tags.isNotEmpty())
        val tagChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_tags)

        tagChipGroup.removeAllViews()
        if(item.entryDisplayItem.tags.isNotEmpty()) {
          item.entryDisplayItem.tags.forEach { tagName ->
            val chip = Chip(itemView.context)
            chip.text = tagName
            tagChipGroup.addView(chip)
          }
        }
      }
    } else {
      tagDisplay.show(false)
    }
  }

  private fun displayBooks() {
    if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_BOOKS, true)) {
      displayedItem?.let { item ->
        bookDisplay.show(item.entryDisplayItem.books.isNotEmpty())
        val bookChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_books)

        bookChipGroup.removeAllViews()
        if(item.entryDisplayItem.books.isNotEmpty()) {
          item.entryDisplayItem.books.forEach { bookName ->
            val chip = Chip(itemView.context)
            chip.text = bookName
            bookChipGroup.addView(chip)
          }
        }
      }
    } else {
      bookDisplay.show(false)
    }
  }
}
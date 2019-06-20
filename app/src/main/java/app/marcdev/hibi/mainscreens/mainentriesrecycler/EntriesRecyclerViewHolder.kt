package app.marcdev.hibi.mainscreens.mainentriesrecycler

import android.content.res.Resources
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_BOOKS
import app.marcdev.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_LOCATION
import app.marcdev.hibi.internal.PREF_MAIN_ENTRY_DISPLAY_TAGS
import app.marcdev.hibi.internal.extension.show
import app.marcdev.hibi.internal.utils.formatDateForDisplay
import app.marcdev.hibi.internal.utils.formatTimeForDisplay
import app.marcdev.hibi.mainscreens.MainScreenFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EntriesRecyclerViewHolder(private val onSelectClick: View.OnClickListener?,
                                itemView: View,
                                private val theme: Resources.Theme)
  : BaseEntriesRecyclerViewHolder(itemView) {

  // <editor-fold desc="UI Components">
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var tagDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_tags)
  private var bookDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_books)
  private var locationDisplay: TextView = itemView.findViewById(R.id.txt_item_location)
  // </editor-fold>

  // <editor-fold desc="Other">
  private var displayedItem: MainEntriesDisplayItem? = null
  private val prefs = PreferenceManager.getDefaultSharedPreferences(itemView.context)
  // </editor-fold>

  private val clickListener = View.OnClickListener {
    val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
    if(displayedItem != null) {
      viewEntryAction.entryId = displayedItem!!.entry.id
    }
    Navigation.findNavController(itemView).navigate(viewEntryAction)
  }

  init {
    itemView.findViewById<ConstraintLayout>(R.id.const_item_main_recycler).setOnClickListener(clickListener)
  }

  override fun display(item: MainEntriesDisplayItem) {
    this.displayedItem = item
    val dateDisplayText = formatDateForDisplay(item.entry.day, item.entry.month, item.entry.year)
    dateDisplay.text = dateDisplayText
    val timeDisplayText = formatTimeForDisplay(item.entry.hour, item.entry.minute)
    timeDisplay.text = timeDisplayText
    contentDisplay.text = item.entry.content
    displayLocation()
    displayTags()
    displayBooks()
    if(item.isSelected) {
      val typedValue = TypedValue()
      theme.resolveAttribute(R.attr.hibiSelectedItemColor, typedValue, true)
      itemView.setBackgroundColor(typedValue.data)
    } else {
      itemView.background = null
    }
    val selectedIcon: ImageView = itemView.findViewById(R.id.img_item_selected)
    selectedIcon.show(item.isSelected)
    selectedIcon.setOnClickListener(onSelectClick)
  }

  private fun displayLocation() {
    if(prefs.getBoolean(PREF_MAIN_ENTRY_DISPLAY_LOCATION, true)) {
      displayedItem?.let { item ->
        if(item.entry.location.isNotBlank()) {
          locationDisplay.text = item.entry.location
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
        tagDisplay.show(item.tags.isNotEmpty())
        val tagChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_tags)

        tagChipGroup.removeAllViews()
        if(item.tags.isNotEmpty()) {
          item.tags.forEach { tagName ->
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
        bookDisplay.show(item.books.isNotEmpty())
        val bookChipGroup = itemView.findViewById<ChipGroup>(R.id.cg_main_books)

        bookChipGroup.removeAllViews()
        if(item.books.isNotEmpty()) {
          item.books.forEach { bookName ->
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
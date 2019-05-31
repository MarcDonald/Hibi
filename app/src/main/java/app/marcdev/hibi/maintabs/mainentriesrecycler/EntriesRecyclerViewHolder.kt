package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EntriesRecyclerViewHolder(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // <editor-fold desc="UI Components">
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var tagChipGroup: ChipGroup = itemView.findViewById(R.id.cg_main_tags)
  private var tagDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_tags)
  private var locationDisplay: TextView = itemView.findViewById(R.id.txt_item_location)
  // </editor-fold>

  // <editor-fold desc="Other">
  private var displayedItem: MainEntriesDisplayItem? = null
  // </editor-fold>

  private val clickListener = View.OnClickListener { view ->
    val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
    if(displayedItem != null) {
      viewEntryAction.entryId = displayedItem!!.entry.id
    }
    Navigation.findNavController(view).navigate(viewEntryAction)
  }

  init {
    itemView.setOnClickListener(clickListener)
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
  }

  private fun displayLocation() {
    displayedItem?.let { item ->
      if(item.entry.location.isNotBlank()) {
        locationDisplay.text = item.entry.location
        locationDisplay.visibility = View.VISIBLE
      } else {
        locationDisplay.visibility = View.GONE
      }
    }
  }

  private fun displayTags() {
    displayedItem?.let { item ->
      tagDisplay.visibility = if(item.tags.isEmpty()) View.GONE else View.VISIBLE

      tagChipGroup.removeAllViews()
      if(item.tags.isNotEmpty()) {
        item.tags.forEach {
          val chip = Chip(itemView.context)
          chip.text = it
          chip.scaleX = 0.9f
          chip.scaleY = 0.9f
          tagChipGroup.addView(chip)
        }
      }
    }
  }
}
package app.marcdev.hibi.mainscreen.mainscreenrecycler

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import app.marcdev.hibi.mainscreen.MainScreenFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EntriesRecyclerViewHolder(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // Entry
  private var displayedItem: MainScreenDisplayItem? = null

  // UI Components
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var chipGroup: ChipGroup = itemView.findViewById(R.id.cg_main_tags)

  private val clickListener = View.OnClickListener {
    val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
    if(displayedItem != null) {
      if(displayedItem!!.entry.id != null) {
        viewEntryAction.entryId = displayedItem!!.entry.id!!
      }
    }
    Navigation.findNavController(it).navigate(viewEntryAction)
  }

  init {
    itemView.setOnClickListener(clickListener)
  }

  override fun display(item: MainScreenDisplayItem) {
    dateDisplay.text = formatDateForDisplay(item.entry.day, item.entry.month, item.entry.year)
    timeDisplay.text = formatTimeForDisplay(item.entry.hour, item.entry.minute)
    contentDisplay.text = item.entry.content

    if(item.tagEntryRelations.isNotEmpty()) {
      item.tagEntryRelations.forEach {
        val chip = Chip(itemView.context)
        chip.text = it.tag
        chipGroup.addView(chip)
      }
    }

    this.displayedItem = item
  }
}
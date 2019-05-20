package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.formatDateTimeForDisplay
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EntriesRecyclerViewHolder(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // Entry
  private var displayedItem: MainEntriesDisplayItem? = null

  // UI Components
  private var dateTimeDisplay: TextView = itemView.findViewById(R.id.item_date_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var chipGroup: ChipGroup = itemView.findViewById(R.id.cg_main_tags)

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
    val dateTimeDisplayText = formatDateTimeForDisplay(item.entry.day, item.entry.month, item.entry.year, item.entry.hour, item.entry.minute)
    dateTimeDisplay.text = dateTimeDisplayText
    contentDisplay.text = item.entry.content

    chipGroup.removeAllViews()
    if(item.tags.isNotEmpty()) {
      item.tags.forEach {
        val chip = Chip(itemView.context)
        chip.text = it
        chipGroup.addView(chip)
      }
    }

    this.displayedItem = item
  }
}
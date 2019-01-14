package app.marcdev.hibi.mainscreen.mainscreenrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.formatDateForHeader

class EntriesRecyclerViewHolderHeader(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // Entry
  private var displayedItem: MainScreenDisplayItem? = null

  // UI Components
  private val dateDisplay: TextView = itemView.findViewById(R.id.txt_header_date)

  override fun display(item: MainScreenDisplayItem) {
    this.displayedItem = item
    dateDisplay.text = formatDateForHeader(item.entry.month, item.entry.year)
  }
}
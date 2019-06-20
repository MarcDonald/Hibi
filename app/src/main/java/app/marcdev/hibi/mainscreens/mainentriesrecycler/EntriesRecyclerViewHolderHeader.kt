package app.marcdev.hibi.mainscreens.mainentriesrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.utils.formatDateForHeader

class EntriesRecyclerViewHolderHeader(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // Entry
  private var displayedItem: MainEntriesDisplayItem? = null

  // UI Components
  private val dateDisplay: TextView = itemView.findViewById(R.id.txt_header_date)

  override fun display(item: MainEntriesDisplayItem) {
    this.displayedItem = item
    dateDisplay.text = formatDateForHeader(item.entry.month, item.entry.year)
  }
}
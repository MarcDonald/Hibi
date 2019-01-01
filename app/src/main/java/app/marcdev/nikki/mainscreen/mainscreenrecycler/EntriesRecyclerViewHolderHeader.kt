package app.marcdev.nikki.mainscreen.mainscreenrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.entity.Entry
import app.marcdev.nikki.internal.formatDateForHeader

class EntriesRecyclerViewHolderHeader(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

  // Entry
  private var displayedEntry: Entry? = null

  // UI Components
  private val dateDisplay: TextView = itemView.findViewById(R.id.txt_header_date)

  override fun display(entry: Entry) {
    this.displayedEntry = entry
    dateDisplay.text = formatDateForHeader(entry.month, entry.year)
  }
}
package app.marcdev.nichiroku.mainscreen

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.data.entity.Entry
import app.marcdev.nichiroku.formatDateForDisplay
import app.marcdev.nichiroku.formatTimeForDisplay

class EntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  // Entry
  private var displayedEntry: Entry? = null

  // UI Components
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)

  fun display(entry: Entry) {
    dateDisplay.text = formatDateForDisplay(entry.day, entry.month, entry.year)
    timeDisplay.text = formatTimeForDisplay(entry.hour, entry.minute)
    contentDisplay.text = entry.content
    this.displayedEntry = entry
  }
}
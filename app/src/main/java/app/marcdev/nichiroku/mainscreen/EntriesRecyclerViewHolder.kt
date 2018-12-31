package app.marcdev.nichiroku.mainscreen

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.data.entity.Entry

class EntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  // Entry
  private var displayedEntry: Entry? = null

  // UI Components
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)

  fun display(entry: Entry) {
    dateDisplay.text = entry.date
    timeDisplay.text = entry.time
    contentDisplay.text = entry.content
    this.displayedEntry = entry
  }
}
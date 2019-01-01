package app.marcdev.nikki.mainscreen

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.entity.Entry
import app.marcdev.nikki.formatDateForDisplay
import app.marcdev.nikki.formatTimeForDisplay

class EntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  // Entry
  private var displayedEntry: Entry? = null

  // UI Components
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)

  private val clickListener = View.OnClickListener {
    val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
    if(displayedEntry != null) {
      if(displayedEntry!!.id != null) {
        viewEntryAction.entryId = displayedEntry!!.id!!
      }
    }
    Navigation.findNavController(it).navigate(viewEntryAction)
  }

  init {
    itemView.setOnClickListener(clickListener)
  }

  fun display(entry: Entry) {
    dateDisplay.text = formatDateForDisplay(entry.day, entry.month, entry.year)
    timeDisplay.text = formatTimeForDisplay(entry.hour, entry.minute)
    contentDisplay.text = entry.content
    this.displayedEntry = entry
  }
}
package app.marcdev.hibi.mainscreen.mainscreenrecycler

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import app.marcdev.hibi.mainscreen.MainScreenFragmentDirections

class EntriesRecyclerViewHolder(itemView: View) : BaseEntriesRecyclerViewHolder(itemView) {

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

  override fun display(entry: Entry) {
    dateDisplay.text = formatDateForDisplay(entry.day, entry.month, entry.year)
    timeDisplay.text = formatTimeForDisplay(entry.hour, entry.minute)
    contentDisplay.text = entry.content
    this.displayedEntry = entry
  }
}
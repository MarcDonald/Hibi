package app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import timber.log.Timber

class TagsFragmentRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private var tagNameDisplay: TextView = itemView.findViewById(R.id.tag_item_name)
  private var tagCountDisplay: TextView = itemView.findViewById(R.id.tag_item_count)

  private var displayedItem: TagDisplayItem? = null

  private val clickListener = View.OnClickListener {
    if(displayedItem != null) {
      val tagName = displayedItem!!.tagName
      val viewEntryAction = MainScreenFragmentDirections.viewTaggedEntriesAction(tagName)
      Navigation.findNavController(it).navigate(viewEntryAction)
    }
  }

  private val longClickListener = View.OnLongClickListener {
    Timber.i("Log: longClickListener: Long Click")
    true
  }

  init {
    itemView.setOnClickListener(clickListener)
    itemView.setOnLongClickListener(longClickListener)
  }

  fun display(item: TagDisplayItem) {
    displayedItem = item
    tagNameDisplay.text = item.tagName
    tagCountDisplay.text = itemView.resources.getString(R.string.tag_use_count_wc, item.useCount.toString())
  }
}
package app.marcdev.hibi.maintabs.tagsfragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import timber.log.Timber

class TagsFragmentRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private var tagNameDisplay: TextView = itemView.findViewById(R.id.tag_item_name)
  private var tagCountDisplay: TextView = itemView.findViewById(R.id.tag_item_count)

  var displayedItem: TagDisplayItem? = null

  private val clickListener = View.OnClickListener {
    Timber.i("Log: clickListener: Click")
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
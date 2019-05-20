package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R

class EntriesRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<BaseEntriesRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var items: List<MainEntriesDisplayItem> = listOf()
  private var lastPosition = -1

  override fun getItemViewType(position: Int): Int {
    // Use day as the view type, if the day is 0 then it's a header, since a day 0 can't be added normally
    return items[position].entry.day
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEntriesRecyclerViewHolder {
    return when(viewType) {
      0 -> {
        val view = inflater.inflate(R.layout.item_main_screen_header, parent, false)
        EntriesRecyclerViewHolderHeader(view)
      }
      else -> {
        val view = inflater.inflate(R.layout.item_main_screen_entry, parent, false)
        EntriesRecyclerViewHolder(view)
      }
    }
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: BaseEntriesRecyclerViewHolder, position: Int) {
    holder.display(items[position])
    setAnimation(holder.itemView, position)
  }

  fun updateList(list: List<MainEntriesDisplayItem>) {
    items = list
    notifyDataSetChanged()
  }

  private fun setAnimation(viewToAnimate: View, position: Int) {
    if(position > lastPosition) {
      val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
      viewToAnimate.startAnimation(animation)
      lastPosition = position
    }
  }
}
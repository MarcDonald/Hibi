package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R

class EntriesRecyclerAdapter(private val context: Context, private val theme: Resources.Theme) : RecyclerView.Adapter<BaseEntriesRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var items: List<MainEntriesDisplayItem> = listOf()
  private var lastPosition = -1
  private var itemsSelectable = false
  private var onSelectClick: View.OnClickListener? = null

  constructor(context: Context, itemsSelectable: Boolean, onSelectClick: View.OnClickListener, theme: Resources.Theme) : this(context, theme) {
    this.itemsSelectable = itemsSelectable
    this.onSelectClick = onSelectClick
  }

  override fun getItemViewType(position: Int): Int {
    // Use day as the view type, if the day is 0 then it's a header, since a day 0 can't be added normally
    return items[position].entry.day
  }

  fun isHeader(itemPosition: Int): Boolean {
    return items[itemPosition].entry.day == 0
  }

  fun getSelectedEntryIds(): List<Int> {
    val list = mutableListOf<Int>()
    items.forEach { item ->
      if(item.isSelected)
        list.add(item.entry.id)
    }
    return list.toList()
  }

  fun clearSelectedEntries() {
    items.forEach { item ->
      item.isSelected = false
    }
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEntriesRecyclerViewHolder {
    return when(viewType) {
      0 -> {
        val view = inflater.inflate(R.layout.item_main_screen_header, parent, false)
        EntriesRecyclerViewHolderHeader(view)
      }
      else -> {
        val view = inflater.inflate(R.layout.item_main_screen_entry, parent, false)
        EntriesRecyclerViewHolder(onSelectClick, view, theme)
      }
    }
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: BaseEntriesRecyclerViewHolder, position: Int) {
    holder.display(items[position])
    if(itemsSelectable) {
      if(!isHeader(position)) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.const_item_main_recycler).setOnLongClickListener {
          items[position].isSelected = !items[position].isSelected
          notifyItemChanged(position)
          true
        }
      }
    }
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
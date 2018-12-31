package app.marcdev.nichiroku.mainscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.data.entity.Entry

class EntriesRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<EntriesRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var entries: List<Entry> = listOf()
  private var lastPosition = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_entry, parent, false)
    return EntriesRecyclerViewHolder(view)
  }

  override fun getItemCount(): Int {
    return entries.size
  }

  override fun onBindViewHolder(holder: EntriesRecyclerViewHolder, position: Int) {
    holder.display(entries[position])
    setAnimation(holder.itemView, position)
  }

  fun updateList(list: List<Entry>) {
    entries = list
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
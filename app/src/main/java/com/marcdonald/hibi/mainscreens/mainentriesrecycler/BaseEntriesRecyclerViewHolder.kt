package com.marcdonald.hibi.mainscreens.mainentriesrecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseEntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  open fun display(item: MainEntriesDisplayItem) {
    // To be overridden
  }
}
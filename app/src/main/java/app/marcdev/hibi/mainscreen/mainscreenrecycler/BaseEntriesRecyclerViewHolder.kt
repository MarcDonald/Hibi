package app.marcdev.hibi.mainscreen.mainscreenrecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.data.entity.Entry

open class BaseEntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  open fun display(entry: Entry) {
    // To be overridden
  }
}
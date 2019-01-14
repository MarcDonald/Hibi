package app.marcdev.hibi.mainscreen.mainscreenrecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseEntriesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  open fun display(item: MainScreenDisplayItem) {
    // To be overridden
  }
}
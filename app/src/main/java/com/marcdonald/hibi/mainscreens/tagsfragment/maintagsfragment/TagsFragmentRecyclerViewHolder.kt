package com.marcdonald.hibi.mainscreens.tagsfragment.maintagsfragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.TAG_ID_KEY
import com.marcdonald.hibi.mainscreens.MainScreenFragmentDirections
import com.marcdonald.hibi.uicomponents.addtagdialog.AddTagDialog

class TagsFragmentRecyclerViewHolder(itemView: View,
                                     private val fragmentManager: FragmentManager)
  : RecyclerView.ViewHolder(itemView) {

  private var tagNameDisplay: TextView = itemView.findViewById(R.id.tag_item_name)
  private var tagCountDisplay: TextView = itemView.findViewById(R.id.tag_item_count)

  private var displayedItem: TagDisplayItem? = null

  private val clickListener = View.OnClickListener {
    if(displayedItem != null) {
      val tagID = displayedItem!!.tagID
      val viewEntryAction = MainScreenFragmentDirections.viewTaggedEntriesAction(tagID)
      Navigation.findNavController(itemView).navigate(viewEntryAction)
    }
  }

  private val longClickListener = View.OnLongClickListener {
    initEditOrDeleteDialog()
    true
  }

  private fun initEditOrDeleteDialog() {
    val editTagDialog = AddTagDialog()
    if(displayedItem != null) {
      val arguments = Bundle()
      arguments.putInt(TAG_ID_KEY, displayedItem!!.tagID)
      editTagDialog.arguments = arguments
    }
    editTagDialog.show(fragmentManager, "Edit or Delete Tag Dialog")
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
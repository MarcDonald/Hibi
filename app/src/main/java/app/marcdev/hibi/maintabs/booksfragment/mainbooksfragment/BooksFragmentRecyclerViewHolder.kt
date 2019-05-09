package app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.BOOK_ID_KEY
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import app.marcdev.hibi.uicomponents.addbookdialog.AddBookDialog

class BooksFragmentRecyclerViewHolder(itemView: View, private val fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView) {

  private var bookNameDisplay: TextView = itemView.findViewById(R.id.book_item_name)
  private var bookCountDisplay: TextView = itemView.findViewById(R.id.book_item_count)

  private var displayedItem: BookDisplayItem? = null

  private val clickListener = View.OnClickListener {
    if(displayedItem != null) {
      val bookID = displayedItem!!.bookId
      val viewEntryAction = MainScreenFragmentDirections.viewBookEntriesAction(bookID)
      Navigation.findNavController(it).navigate(viewEntryAction)
    }
  }

  private val longClickListener = View.OnLongClickListener {
    initEditOrDeleteDialog()
    true
  }

  private fun initEditOrDeleteDialog() {
    val editBookDialog = AddBookDialog()
    if(displayedItem != null) {
      val arguments = Bundle()
      arguments.putInt(BOOK_ID_KEY, displayedItem!!.bookId)
      editBookDialog.arguments = arguments
    }
    editBookDialog.show(fragmentManager, "Edit or Delete Book Dialog")
  }

  init {
    itemView.setOnClickListener(clickListener)
    itemView.setOnLongClickListener(longClickListener)
  }

  fun display(item: BookDisplayItem) {
    displayedItem = item
    bookNameDisplay.text = item.bookName
    bookCountDisplay.text = itemView.resources.getString(R.string.book_use_count_wc, item.useCount.toString())
  }
}
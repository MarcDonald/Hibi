package app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R

class BooksRecyclerAdapter(private val context: Context, private val fragmentManager: FragmentManager)
  : RecyclerView.Adapter<BooksFragmentRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var items: List<BookDisplayItem> = listOf()
  private var lastPosition = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksFragmentRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_book, parent, false)
    return BooksFragmentRecyclerViewHolder(view, fragmentManager)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: BooksFragmentRecyclerViewHolder, position: Int) {
    holder.display(items[position])
    setAnimation(holder.itemView, position)
  }

  fun updateList(list: List<BookDisplayItem>) {
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
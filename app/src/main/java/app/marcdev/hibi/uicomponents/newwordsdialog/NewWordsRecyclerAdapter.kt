package app.marcdev.hibi.uicomponents.newwordsdialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.data.entity.NewWord

class NewWordsRecyclerAdapter(private val context: Context,
                              private val fragmentManager: FragmentManager)
  : RecyclerView.Adapter<NewWordsRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var dataList: List<NewWord> = listOf()
  private var lastPosition = -1
  var isEditMode = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewWordsRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_new_word, parent, false)
    return NewWordsRecyclerViewHolder(view, fragmentManager, isEditMode)
  }

  override fun getItemCount(): Int {
    return dataList.size
  }

  override fun onBindViewHolder(holder: NewWordsRecyclerViewHolder, position: Int) {
    holder.display(dataList[position])
    setAnimation(holder.itemView, position)
  }

  fun updateList(list: List<NewWord>) {
    dataList = list
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
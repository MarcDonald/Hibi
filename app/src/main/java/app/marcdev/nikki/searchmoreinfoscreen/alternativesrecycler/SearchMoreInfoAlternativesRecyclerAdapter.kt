package app.marcdev.nikki.searchmoreinfoscreen.alternativesrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Japanese

class SearchMoreInfoAlternativesRecyclerAdapter(context: Context) : RecyclerView.Adapter<SearchMoreInfoAlternativesRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var japaneseList: List<Japanese> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoreInfoAlternativesRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_search_result_more_info_alternative, parent, false)
    return SearchMoreInfoAlternativesRecyclerViewHolder(view)
  }

  override fun getItemCount(): Int {
    return japaneseList.size
  }

  override fun onBindViewHolder(holder: SearchMoreInfoAlternativesRecyclerViewHolder, position: Int) {
    holder.display(japaneseList[position])
  }

  fun updateList(list: List<Japanese>) {
    japaneseList = list
    notifyDataSetChanged()
  }
}
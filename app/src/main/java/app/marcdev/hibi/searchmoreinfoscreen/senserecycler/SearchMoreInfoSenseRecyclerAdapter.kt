package app.marcdev.hibi.searchmoreinfoscreen.senserecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.data.network.apiresponse.Sense

class SearchMoreInfoSenseRecyclerAdapter(context: Context) : RecyclerView.Adapter<SearchMoreInfoSenseRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var list: List<Sense> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoreInfoSenseRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_search_result_more_info_sense, parent, false)
    return SearchMoreInfoSenseRecyclerViewHolder(view)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(holder: SearchMoreInfoSenseRecyclerViewHolder, position: Int) {
    holder.display(list[position])
  }

  fun updateList(list: List<Sense>) {
    this.list = list
    notifyDataSetChanged()
  }
}
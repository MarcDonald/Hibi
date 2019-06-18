package app.marcdev.hibi.search.searchresults

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.data.network.jisho.apiresponse.Data

class SearchResultsRecyclerAdapter(context: Context, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<SearchResultsRecyclerViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var dataList: List<Data> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_search_result, parent, false)
    return SearchResultsRecyclerViewHolder(view, fragmentManager)
  }

  override fun getItemCount(): Int {
    return dataList.size
  }

  override fun onBindViewHolder(holder: SearchResultsRecyclerViewHolder, position: Int) {
    holder.display(dataList[position])
  }

  fun updateList(list: List<Data>) {
    dataList = list
    notifyDataSetChanged()
  }
}
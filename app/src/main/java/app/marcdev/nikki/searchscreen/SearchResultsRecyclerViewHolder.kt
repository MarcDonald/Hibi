package app.marcdev.nikki.searchscreen

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Data

open class SearchResultsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_result_word)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_result_reading)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_search_result_english)

  open fun display(data: Data) {
    wordDisplay.text = data.japanese[0].word
    readingDisplay.text = data.japanese[0].reading
    englishDisplay.text = data.senses[0].englishDefinitions[0]
  }
}
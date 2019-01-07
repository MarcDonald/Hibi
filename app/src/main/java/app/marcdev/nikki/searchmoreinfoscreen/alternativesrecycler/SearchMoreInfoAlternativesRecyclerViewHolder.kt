package app.marcdev.nikki.searchmoreinfoscreen.alternativesrecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Japanese

class SearchMoreInfoAlternativesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_word)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_reading)

  private var displayedJapanese: Japanese? = null

  fun display(japanese: Japanese) {
    displayedJapanese = japanese
    displayJapanese(japanese)
  }

  private fun displayJapanese(japanese: Japanese) {
    val word: String? = japanese.word
    val reading: String? = japanese.reading

    if(word == null || word.isBlank()) {
      wordDisplay.text = itemView.resources.getString(R.string.search_results_word, reading)
      readingDisplay.visibility = View.GONE
    } else {
      wordDisplay.text = itemView.resources.getString(R.string.search_results_word, word)
      if(reading == null || reading.isBlank()) {
        readingDisplay.visibility = View.GONE
      } else {
        readingDisplay.text = itemView.resources.getString(R.string.search_results_reading, reading)
      }
    }
  }
}
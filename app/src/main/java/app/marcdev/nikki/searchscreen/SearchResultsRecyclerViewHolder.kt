package app.marcdev.nikki.searchscreen

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Data

class SearchResultsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_result_word)
  private val typeDisplay: TextView = itemView.findViewById(R.id.txt_search_result_type)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_result_reading)
  private val otherFormsDisplay: TextView = itemView.findViewById(R.id.txt_search_result_other_forms)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_search_result_english)

  fun display(data: Data) {

    displayJapanese(data)
    displayType(data)
    displayEnglish(data)
    displayOtherForms(data)
  }

  private fun displayJapanese(data: Data) {
    val word: String? = data.japanese[0].word
    val reading: String? = data.japanese[0].reading

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

  private fun displayType(data: Data) {
    var typeString = ""
    if(data.senses.isNotEmpty()) {
      if(data.senses[0].partsOfSpeech.isNotEmpty()) {
        typeString += data.senses[0].partsOfSpeech[0]
      }
    } else {
      typeDisplay.visibility = View.GONE
    }

    typeDisplay.text = typeString
  }

  private fun displayEnglish(data: Data) {
    var englishDisplayString = ""
    for(x in 0 until data.senses.size) {
      for(y in 0 until data.senses[x].englishDefinitions.size) {
        englishDisplayString += "${data.senses[x].englishDefinitions[y]}; "
      }
    }
    englishDisplay.text = itemView.resources.getString(R.string.search_results_english, englishDisplayString)
  }

  private fun displayOtherForms(data: Data) {
    if(data.japanese.size > 1) {
      otherFormsDisplay.visibility = View.VISIBLE
    } else {
      otherFormsDisplay.visibility = View.GONE
    }
  }
}
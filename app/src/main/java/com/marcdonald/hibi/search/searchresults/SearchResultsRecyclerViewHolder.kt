package com.marcdonald.hibi.search.searchresults

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Data
import com.marcdonald.hibi.search.searchmoreinfoscreen.SearchMoreInfoDialog

class SearchResultsRecyclerViewHolder(itemView: View, fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_result_word)
  private val typeDisplay: TextView = itemView.findViewById(R.id.txt_search_result_type)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_result_reading)
  private val otherFormsDisplay: TextView = itemView.findViewById(R.id.txt_search_result_other_forms)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_search_result_english)
  private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.const_search_result)

  private var displayedData: Data? = null

  private val clickListener = View.OnClickListener {
    val dialog = SearchMoreInfoDialog()

    val listOfJapaneseJson = getAllJapaneseAsJson()
    val listOfSensesJson = getAllSensesAsJson()
    val bundle = Bundle()
    bundle.putStringArrayList("japaneseList", listOfJapaneseJson)
    bundle.putStringArrayList("sensesList", listOfSensesJson)

    dialog.arguments = bundle
    dialog.show(fragmentManager, "Search Result More Info Dialog")
  }

  init {
    constraintLayout.setOnClickListener(clickListener)
  }

  fun display(data: Data) {
    displayedData = data
    displayJapanese(data)
    displayType(data)
    displayEnglish(data)
    displayOtherForms(data)
  }

  private fun displayJapanese(data: Data) {
    val word: String? = data.japanese[0].word
    val reading: String? = data.japanese[0].reading

    // If no mainWord is supplied, use the reading as the main word and hide the reading display
    if(word == null || word.isBlank()) {
      wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, reading)
      readingDisplay.visibility = View.GONE
    } else {
      // If a mainWord is displayed, just it as the main word
      wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, word)
      if(reading == null || reading.isBlank()) {
        // If no mainReading is supplied then hide the reading field
        readingDisplay.visibility = View.GONE
      } else {
        // Otherwise use the reading
        readingDisplay.text = itemView.resources.getString(R.string.reading_wc, reading)
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
    val definitionList = mutableListOf<String>()

    for(x in 0 until data.senses.size) {
      for(y in 0 until data.senses[x].englishDefinitions.size) {
        val englishDefinition = data.senses[x].englishDefinitions[y]
        // If the definition isn't already being displayed
        if(!definitionList.contains(englishDefinition.toLowerCase())) {
          englishDisplayString += if(x == data.senses.size - 1 && y == data.senses[x].englishDefinitions.size - 1) {
            "$englishDefinition "
          } else {
            "$englishDefinition; "
          }
          definitionList.add(englishDefinition.toLowerCase())
        }
      }
    }
    englishDisplay.text = englishDisplayString
  }

  private fun displayOtherForms(data: Data) {
    if(data.japanese.size > 1) {
      otherFormsDisplay.visibility = View.VISIBLE
    } else {
      otherFormsDisplay.visibility = View.GONE
    }
  }

  private fun getAllJapaneseAsJson(): ArrayList<String> {
    val listOfJapaneseJson = ArrayList<String>()

    displayedData?.let {
      if(displayedData!!.japanese.isNotEmpty()) {
        for(x in 0 until displayedData!!.japanese.size) {
          val japaneseJson = Gson().toJson(displayedData!!.japanese[x])
          listOfJapaneseJson.add(japaneseJson)
        }
      }
    }

    return listOfJapaneseJson
  }

  private fun getAllSensesAsJson(): ArrayList<String> {
    val listOfSensesJson = ArrayList<String>()

    displayedData?.let {
      if(displayedData!!.senses.isNotEmpty()) {
        for(x in 0 until displayedData!!.senses.size) {
          val sensesJson = Gson().toJson(displayedData!!.senses[x])
          listOfSensesJson.add(sensesJson)
        }
      }
    }

    return listOfSensesJson
  }
}
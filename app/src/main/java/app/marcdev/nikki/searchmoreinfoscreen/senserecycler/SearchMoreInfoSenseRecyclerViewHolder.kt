package app.marcdev.nikki.searchmoreinfoscreen.senserecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Sense

class SearchMoreInfoSenseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val typeDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_type)
  private val tagDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_tags)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_english)
  private val restrictionsDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_restrictions)
  private val antonymsDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_antonyms)
  private val seeAlsoDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_see_also)

  fun display(sense: Sense) {
    displayType(sense)
    displayTags(sense)
    displayEnglish(sense)
    displayRestrictions(sense)
    displayAntonyms(sense)
    displaySeeAlso(sense)
  }

  private fun displayType(sense: Sense) {
    if(sense.partsOfSpeech.isNotEmpty()) {
      var displayString = ""
      for(x in 0 until sense.partsOfSpeech.size) {
        displayString += if(x == sense.partsOfSpeech.size - 1)
          "${sense.partsOfSpeech[x]} "
        else
          "${sense.partsOfSpeech[x]}; "
      }
      typeDisplay.text = displayString
    } else {
      typeDisplay.visibility = View.GONE
    }
  }

  private fun displayTags(sense: Sense) {
    if(sense.tags.isNotEmpty()) {
      var displayString = ""
      for(x in 0 until sense.tags.size) {
        displayString += if(x == sense.tags.size - 1)
          "${sense.tags[x]} "
        else
          "${sense.tags[x]}; "
      }
      tagDisplay.text = displayString
    } else {
      tagDisplay.visibility = View.GONE
    }
  }

  private fun displayEnglish(sense: Sense) {
    if(sense.englishDefinitions.isNotEmpty()) {
      var displayString = ""
      for(x in 0 until sense.englishDefinitions.size) {
        displayString += if(x == sense.englishDefinitions.size - 1)
          "${sense.englishDefinitions[x]} "
        else
          "${sense.englishDefinitions[x]}; "
      }
      englishDisplay.text = displayString
    } else {
      englishDisplay.visibility = View.GONE
    }
  }

  private fun displayRestrictions(sense: Sense) {
    if(sense.restrictions.isNotEmpty()) {
      var restrictionsString = ""
      for(x in 0 until sense.restrictions.size) {
        restrictionsString += if(x == sense.restrictions.size - 1)
          "${sense.restrictions[x]} "
        else
          "${sense.restrictions[x]}; "
      }
      val displayString = itemView.resources.getString(R.string.search_results_restrictions, restrictionsString)
      restrictionsDisplay.text = displayString
    } else {
      restrictionsDisplay.visibility = View.GONE
    }
  }

  private fun displayAntonyms(sense: Sense) {
    if(sense.antonyms.isNotEmpty()) {
      var antonymsString = ""
      for(x in 0 until sense.antonyms.size) {
        antonymsString += if(x == sense.antonyms.size - 1)
          "${sense.antonyms[x]} "
        else
          "${sense.antonyms[x]}; "
      }
      val displayString = itemView.resources.getString(R.string.search_results_antonyms, antonymsString)
      antonymsDisplay.text = displayString
    } else {
      antonymsDisplay.visibility = View.GONE
    }
  }

  private fun displaySeeAlso(sense: Sense) {
    if(sense.seeAlso.isNotEmpty()) {
      var seeAlsoString = ""
      for(x in 0 until sense.seeAlso.size) {
        seeAlsoString += if(x == sense.seeAlso.size - 1)
          "${sense.seeAlso[x]} "
        else
          "${sense.seeAlso[x]}; "
      }
      val displayString = itemView.resources.getString(R.string.search_results_see_also, seeAlsoString)
      seeAlsoDisplay.text = displayString
    } else {
      seeAlsoDisplay.visibility = View.GONE
    }
  }
}
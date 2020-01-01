/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.screens.search.searchresults

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Data
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.screens.search.searchmoreinfo.SearchMoreInfoDialog
import java.util.*
import kotlin.collections.ArrayList

class SearchResultsRecyclerViewHolder(itemView: View, fragmentManager: FragmentManager, private val entryId: Int) :
		RecyclerView.ViewHolder(itemView) {

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
		bundle.putInt(ENTRY_ID_KEY, entryId)

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
				if(!definitionList.contains(englishDefinition.toLowerCase(Locale.getDefault()))) {
					englishDisplayString += if(x == data.senses.size - 1 && y == data.senses[x].englishDefinitions.size - 1) {
						"$englishDefinition "
					} else {
						"$englishDefinition; "
					}
					definitionList.add(englishDefinition.toLowerCase(Locale.getDefault()))
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
/*
 * Copyright 2021 Marc Donald
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
package com.marcdonald.hibi.screens.search.searchmoreinfo.senserecycler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Sense

class SearchMoreInfoSenseRecyclerViewHolder(itemView: View)
	: RecyclerView.ViewHolder(itemView) {

	private val typeDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_type)
	private val tagDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_tags)
	private val englishDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_english)
	private val restrictionsDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_restrictions)
	private val antonymsDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_antonyms)
	private val seeAlsoDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_sense_see_also)

	private var seeAlsoContent = ""
	private var antonymContent = ""

	private val seeAlsoClickListener = View.OnClickListener {
		val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip: ClipData = ClipData.newPlainText("See Also", seeAlsoContent)
		clipboard.setPrimaryClip(clip)

		val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard_wc, seeAlsoContent)
		Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
	}

	private val antonymClickListener = View.OnClickListener {
		val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip: ClipData = ClipData.newPlainText("Antonym", antonymContent)
		clipboard.setPrimaryClip(clip)

		val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard_wc, antonymContent)
		Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
	}

	init {
		seeAlsoDisplay.setOnClickListener(seeAlsoClickListener)
		antonymsDisplay.setOnClickListener(antonymClickListener)
	}

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
			val displayString = itemView.resources.getString(R.string.search_results_restrictions_wc, restrictionsString)
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
			val displayString = itemView.resources.getString(R.string.search_results_antonyms_wc, antonymsString)
			antonymsDisplay.text = displayString
			// Set the antonym content that would be copied to clipboard if clicked
			antonymContent = antonymsString
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
			val displayString = itemView.resources.getString(R.string.search_results_see_also_wc, seeAlsoString)
			seeAlsoDisplay.text = displayString
			// Set the see also content that would be copied to clipboard if clicked
			seeAlsoContent = seeAlsoString
		} else {
			seeAlsoDisplay.visibility = View.GONE
		}
	}
}
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
package com.marcdonald.hibi.screens.search.searchmoreinfo.alternativesrecycler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Japanese

class SearchMoreInfoAlternativesRecyclerViewHolder(itemView: View)
	: RecyclerView.ViewHolder(itemView) {

	private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_word)
	private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_reading)

	private var displayedJapanese: Japanese? = null
	private var wordContent = ""
	private var readingContent = ""

	private val wordClickListener = View.OnClickListener {
		val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip: ClipData = ClipData.newPlainText("Alternative Word", wordContent)
		clipboard.setPrimaryClip(clip)

		val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard_wc, wordContent)
		Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
	}

	private val readingClickListener = View.OnClickListener {
		val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip: ClipData = ClipData.newPlainText("Alternative Reading", readingContent)
		clipboard.setPrimaryClip(clip)

		val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard_wc, readingContent)
		Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
	}

	init {
		wordDisplay.setOnClickListener(wordClickListener)
		readingDisplay.setOnClickListener(readingClickListener)
	}

	fun display(japanese: Japanese) {
		displayedJapanese = japanese
		displayJapanese(japanese)
	}

	private fun displayJapanese(japanese: Japanese) {
		val word: String? = japanese.word
		val reading: String? = japanese.reading

		if(reading != null) {
			if(word == null || word.isBlank()) {
				wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, reading)
				readingDisplay.visibility = View.GONE
				wordContent = reading
			} else {
				wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, word)
				wordContent = word
				if(reading.isBlank()) {
					readingDisplay.visibility = View.GONE
				} else {
					readingDisplay.text = itemView.resources.getString(R.string.reading_wc, reading)
					readingContent = reading
				}
			}
		}
	}
}
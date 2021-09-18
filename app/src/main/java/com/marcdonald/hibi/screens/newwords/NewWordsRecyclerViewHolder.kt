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
package com.marcdonald.hibi.screens.newwords

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.entity.NewWord
import com.marcdonald.hibi.internal.extension.show
import timber.log.Timber

class NewWordsRecyclerViewHolder(itemView: View, private val onClick: (Int) -> Unit)
	: RecyclerView.ViewHolder(itemView) {

	private val wordDisplay: TextView = itemView.findViewById(R.id.txt_new_word_word)
	private val typeDisplay: TextView = itemView.findViewById(R.id.txt_new_word_part)
	private val readingDisplay: TextView = itemView.findViewById(R.id.txt_new_word_reading)
	private val englishDisplay: TextView = itemView.findViewById(R.id.txt_new_word_english)
	private val notesDisplay: TextView = itemView.findViewById(R.id.txt_new_word_notes)
	private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.const_new_word)

	private var displayedNewWord: NewWord? = null

	init {
		constraintLayout.setOnClickListener {
			displayedNewWord?.let { newWord ->
				onClick(newWord.entryId)
			}
		}
	}

	fun display(newWord: NewWord) {
		displayedNewWord = newWord

		if(newWord.word.isNotBlank())
			wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, newWord.word)
		else
			wordDisplay.show(false)

		if(newWord.partOfSpeech.isNotBlank())
			typeDisplay.text = newWord.partOfSpeech
		else
			typeDisplay.show(false)

		if(newWord.reading.isNotBlank())
			readingDisplay.text = itemView.resources.getString(R.string.reading_wc, newWord.reading)
		else
			readingDisplay.show(false)

		if(newWord.english.isNotBlank())
			englishDisplay.text = newWord.english
		else
			englishDisplay.show(false)

		if(newWord.notes.isNotBlank())
			notesDisplay.text = itemView.resources.getString(R.string.notes_wc, newWord.notes)
		else
			notesDisplay.show(false)
	}
}
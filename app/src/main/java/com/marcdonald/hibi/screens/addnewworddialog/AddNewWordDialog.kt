/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi.screens.addnewworddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.*
import com.marcdonald.hibi.internal.base.HibiDialogFragment

class AddNewWordDialog : HibiDialogFragment() {

	private val viewModel by viewModels<AddNewWordViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var dialogTitle: TextView
	private lateinit var wordInput: EditText
	private lateinit var readingInput: EditText
	private lateinit var typeInput: EditText
	private lateinit var englishInput: EditText
	private lateinit var notesInput: EditText
	// </editor-fold>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_add_new_word, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let { arguments ->
			fillFromQuickAdd(arguments)
			val isQuickAdd = arguments.getString(NEW_WORD_QUICK_ADD) != null
			viewModel.passArguments(arguments.getInt(ENTRY_ID_KEY, 0), arguments.getInt(NEW_WORD_ID_KEY, 0), isQuickAdd)
		}
	}

	private fun fillFromQuickAdd(arguments: Bundle) {
		val quickAddWord = arguments.getString(NEW_WORD_QUICK_ADD)
		if(quickAddWord != null) wordInput.setText(quickAddWord)

		val quickAddReading = arguments.getString(NEW_WORD_READING_QUICK_ADD)
		if(quickAddReading != null) readingInput.setText(quickAddReading)

		val quickAddPart = arguments.getStringArrayList(NEW_WORD_PART_QUICK_ADD)
		if(quickAddPart != null) typeInput.setText(viewModel.getSingleStringFromList(quickAddPart))

		val quickAddEnglish = arguments.getStringArrayList(NEW_WORD_MEANING_QUICK_ADD)
		if(quickAddEnglish != null) englishInput.setText(viewModel.getSingleStringFromList(quickAddEnglish))
	}

	private fun bindViews(view: View) {
		wordInput = view.findViewById(R.id.edt_new_word_word)
		readingInput = view.findViewById(R.id.edt_new_word_reading)
		typeInput = view.findViewById(R.id.edt_new_word_type)
		englishInput = view.findViewById(R.id.edt_new_word_english)
		notesInput = view.findViewById(R.id.edt_new_word_notes)
		dialogTitle = view.findViewById(R.id.txt_add_new_word_title)

		val saveButton: MaterialButton = view.findViewById(R.id.btn_save_new_word)
		saveButton.setOnClickListener {
			viewModel.saveNewWord(wordInput.text.toString(), readingInput.text.toString(), typeInput.text.toString(), englishInput.text.toString(), notesInput.text.toString())
		}

		val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_new_word)
		deleteButton.setOnClickListener {
			viewModel.deleteNewWord()
		}
	}

	private fun setupObservers() {
		viewModel.word.observe(viewLifecycleOwner, Observer { value ->
			value?.let { newWord ->
				wordInput.setText(newWord.word)
				readingInput.setText(newWord.reading)
				typeInput.setText(newWord.partOfSpeech)
				englishInput.setText(newWord.english)
				notesInput.setText(newWord.notes)
			}
		})

		viewModel.isEditMode.observe(viewLifecycleOwner, Observer { value ->
			value?.let { isEditMode ->
				if(isEditMode)
					dialogTitle.text = resources.getString(R.string.edit_new_word)
			}
		})

		viewModel.displayEmptyInputWarning.observe(viewLifecycleOwner, Observer { value ->
			value?.let { show ->
				if(show)
					wordInput.error = resources.getString(R.string.empty_input_new_word)
			}
		})

		viewModel.dismiss.observe(viewLifecycleOwner, Observer { value ->
			value?.let { dismiss ->
				if(dismiss)
					dismiss()
			}
		})

		viewModel.showAddedToast.observe(viewLifecycleOwner, Observer { value ->
			value?.let { show ->
				if(show)
					Toast.makeText(requireContext(), getString(R.string.new_word_added), Toast.LENGTH_SHORT).show()
			}
		})
	}
}
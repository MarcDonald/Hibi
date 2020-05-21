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
package com.marcdonald.hibi.screens.entries.addentry.addtagtoentrydialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.ENTRY_ID_KEY
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.screens.addtagdialog.AddTagDialog
import com.marcdonald.hibi.uicomponents.views.CheckBoxWithId

class AddTagToEntryDialog : HibiBottomSheetDialogFragment() {

	private val viewModel by viewModels<AddTagToEntryViewModel> { viewModelFactory }

	// <editor-fold desc="UI Components">
	private lateinit var title: TextView
	private lateinit var tagHolder: LinearLayout
	private lateinit var noTagsWarning: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			viewModel.passArguments(requireArguments().getInt(ENTRY_ID_KEY, 0))
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_entry_tags, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		title = view.findViewById(R.id.txt_add_tags_title)
		tagHolder = view.findViewById(R.id.lin_tags_entry_tag_holder)
		noTagsWarning = view.findViewById(R.id.txt_no_tags_warning)

		val addButton: MaterialButton = view.findViewById(R.id.btn_add_tag)
		addButton.setOnClickListener {
			val dialog = AddTagDialog()
			dialog.show(requireFragmentManager(), "Tag Manager Dialog")
		}

		val saveButton: MaterialButton = view.findViewById(R.id.btn_save_tag)
		saveButton.setOnClickListener(saveClickListener)
	}

	private fun setupObservers() {
		viewModel.allTags.observe(this, Observer { value ->
			value?.let { tags ->
				viewModel.listReceived(tags.isEmpty())

				tags.forEach { tag ->
					// Gets list of all tags currently displayed
					val alreadyDisplayedTags = ArrayList<CheckBoxWithId>()
					for(x in 0 until tagHolder.childCount) {
						val tagCheckBox = tagHolder.getChildAt(x) as CheckBoxWithId
						alreadyDisplayedTags.add(tagCheckBox)
					}

					val displayTag = CheckBoxWithId(tagHolder.context)
					displayTag.text = tag.name
					displayTag.itemId = tag.id
					if(theme == R.style.Theme_Hibi_BottomSheetDialog_Dark) {
						displayTag.setTextColor(resources.getColor(R.color.darkThemePrimaryText, null))
					} else {
						displayTag.setTextColor(resources.getColor(R.color.lightThemePrimaryText, null))
					}

					// If the new tag is already displayed, don't add it
					// This stops it removing user progress before saving
					var addIt = true
					alreadyDisplayedTags.forEach { alreadyDisplayedTag ->
						if(alreadyDisplayedTag.itemId == displayTag.itemId) {
							addIt = false
						}
					}

					if(addIt)
						tagHolder.addView(displayTag)
				}
			}
		})

		viewModel.displayNoTagsWarning.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noTagsWarning.show(shouldShow)
			}
		})

		viewModel.tagEntryRelations.observe(this, Observer { value ->
			value?.let { tagEntryRelations ->
				tagEntryRelations.forEach { tagId ->
					for(x in 0 until tagHolder.childCount) {
						val tagCheckBox = tagHolder.getChildAt(x) as CheckBoxWithId
						if(tagCheckBox.itemId == tagId)
							tagCheckBox.isChecked = true
					}
				}
			}
		})

		viewModel.dismiss.observe(this, Observer { value ->
			value?.let { dismiss ->
				if(dismiss)
					dismiss()
			}
		})
	}

	private val saveClickListener = View.OnClickListener {
		val list = arrayListOf<Pair<Int, Boolean>>()
		for(x in 0 until tagHolder.childCount) {
			val checkBox = tagHolder.getChildAt(x) as CheckBoxWithId
			list.add(Pair(checkBox.itemId, checkBox.isChecked))
		}
		viewModel.onSaveClick(list)
	}
}
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
package com.marcdonald.hibi.uicomponents.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnKeyListener
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.marcdonald.hibi.R

class SearchBar(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
		ConstraintLayout(context, attributeSet, defStyle) {

	private var editText: EditText
	private var searchButton: ImageView

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val view = inflater.inflate(R.layout.view_search_bar, this, true)
		editText = view.findViewById(R.id.edt_search_bar)
		searchButton = view.findViewById(R.id.img_search_button)

		if(attributeSet != null) {
			val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SearchBar, defStyle, 0)

			val hint = attributes.getString(R.styleable.SearchBar_sbHint)
			editText.hint = hint

			attributes.recycle()
		}
	}

	constructor(context: Context) : this(context, null)

	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

	fun setHint(hint: String) {
		editText.hint = hint
	}

	fun setSearchAction(searchAction: (searchTerm: String) -> Unit) {
		val buttonClickListener = OnClickListener {
			search(searchAction)
		}
		searchButton.setOnClickListener(buttonClickListener)

		val searchOnEnterListener = OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
			if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
				search(searchAction)
			}
			false
		}
		editText.setOnKeyListener(searchOnEnterListener)
	}

	fun search(searchAction: (searchTerm: String) -> Unit) {
		val input = editText.text.toString()
		if(input.isNotBlank()) {
			searchAction(input)
			editText.setText("")
		} else {
			editText.error = resources.getString(R.string.no_search_term_warning)
		}
	}
}
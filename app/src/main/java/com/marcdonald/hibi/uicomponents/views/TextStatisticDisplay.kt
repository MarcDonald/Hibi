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
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.extension.show

class TextStatisticDisplay(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
		MaterialCardView(context, attributeSet, defStyle) {

	private var titleText: TextView
	private var messageText: TextView
	private var secondaryMessageText: TextView
	private var linearLayout: LinearLayout

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val view = inflater.inflate(R.layout.view_text_statistic, this, true)
		titleText = view.findViewById(R.id.txt_text_statistic_title)
		messageText = view.findViewById(R.id.txt_text_statistic_message)
		secondaryMessageText = view.findViewById(R.id.txt_text_statistic_secondary_message)
		linearLayout = view.findViewById(R.id.lin_text_statistic)

		if(attributeSet != null) {
			val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TextStatisticDisplay, defStyle, 0)

			val title = attributes.getString(R.styleable.TextStatisticDisplay_tsTitle)
			titleText.text = title

			val message = attributes.getString(R.styleable.TextStatisticDisplay_tsMessage)
			messageText.text = message

			val secondaryMessage = attributes.getString(R.styleable.TextStatisticDisplay_tsSecondaryMessage)
			if(secondaryMessage != null)
				secondaryMessageText.text = message
			else
				secondaryMessageText.show(false)

			attributes.recycle()
		}
	}

	fun setTitle(title: String) {
		titleText.text = title
	}

	fun setMessage(message: String) {
		messageText.text = message
	}

	fun setSecondaryMessage(message: String) {
		if(message.isNotBlank()) {
			secondaryMessageText.show(true)
			secondaryMessageText.text = message
		} else {
			secondaryMessageText.show(false)
		}
	}

	fun showSecondaryMessage(show: Boolean) {
		secondaryMessageText.show(show)
	}

	override fun setOnClickListener(l: OnClickListener?) {
		linearLayout.setOnClickListener(l)
	}

	constructor(context: Context) : this(context, null)

	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
}
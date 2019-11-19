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
package com.marcdonald.hibi.uicomponents.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.marcdonald.hibi.R

class LicenseDisplay(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
		MaterialCardView(context, attributeSet, defStyle) {

	private var titleText: TextView
	private var descriptionText: TextView
	private var licenseText: TextView

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		val view = inflater.inflate(R.layout.view_license, this, true)
		titleText = view.findViewById(R.id.txt_license_title)
		descriptionText = view.findViewById(R.id.txt_license_description)
		licenseText = view.findViewById(R.id.txt_license_license)

		if(attributeSet != null) {
			val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.LicenseDisplay, defStyle, 0)

			val title = attributes.getString(R.styleable.LicenseDisplay_ldTitle)
			titleText.text = title

			val description = attributes.getString(R.styleable.LicenseDisplay_ldDescription)
			descriptionText.text = description

			val license = attributes.getString(R.styleable.LicenseDisplay_ldLicense)
			licenseText.text = license

			attributes.recycle()
		}
	}

	constructor(context: Context) : this(context, null)

	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
}
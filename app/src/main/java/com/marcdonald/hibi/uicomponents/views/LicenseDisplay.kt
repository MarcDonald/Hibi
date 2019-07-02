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

			val title = attributes.getString(R.styleable.LicenseDisplay_title)
			titleText.text = title

			val description = attributes.getString(R.styleable.LicenseDisplay_description)
			descriptionText.text = description

			val license = attributes.getString(R.styleable.LicenseDisplay_license)
			licenseText.text = license

			attributes.recycle()
		}
	}

	constructor(context: Context) : this(context, null)

	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
}
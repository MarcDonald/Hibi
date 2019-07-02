package com.marcdonald.hibi.mainscreens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.marcdonald.hibi.R

class AboutFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_about, container, false)
		view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.about)

		view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
			requireActivity().onBackPressed()
		}
		return view
	}
}
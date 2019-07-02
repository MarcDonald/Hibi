package com.marcdonald.hibi.mainscreens.entryscreens.viewentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marcdonald.hibi.R

class FullscreenImageFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
		loadImage(view)
		return view
	}

	private fun loadImage(view: View) {
		var imagePath = ""
		val imageView: ImageView = view.findViewById(R.id.img_fullscreen_image)
		arguments?.let { arguments ->
			imagePath = FullscreenImageFragmentArgs.fromBundle(arguments).imagePath
		}
		Glide.with(this)
			.load(imagePath)
			.apply(RequestOptions().fitCenter())
			.apply(RequestOptions().error(resources.getDrawable(R.drawable.ic_error_24dp, requireActivity().theme)))
			.into(imageView)
	}
}
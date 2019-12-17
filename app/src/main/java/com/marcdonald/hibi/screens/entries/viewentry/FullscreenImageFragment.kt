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
package com.marcdonald.hibi.screens.entries.viewentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiFragment

class FullscreenImageFragment : HibiFragment() {

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
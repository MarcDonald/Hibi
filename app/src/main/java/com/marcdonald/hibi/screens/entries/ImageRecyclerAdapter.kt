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
package com.marcdonald.hibi.screens.entries

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marcdonald.hibi.R
import com.marcdonald.hibi.screens.mainentriesrecycler.BaseEntriesRecyclerViewHolder

class ImageRecyclerAdapter(private val onItemClick: (String) -> Unit,
													 private val onItemLongClick: (String) -> Unit,
													 context: Context,
													 private val theme: Resources.Theme)
	: RecyclerView.Adapter<ImageRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var items: List<String> = listOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_image, parent, false)
		return ImageRecyclerViewHolder(view, onItemClick, onItemLongClick, theme)
	}

	override fun onBindViewHolder(holder: ImageRecyclerViewHolder, position: Int) {
		holder.display(items[position])
	}

	override fun getItemCount(): Int {
		return items.size
	}

	fun updateItems(newItems: List<String>) {
		items = newItems
		notifyDataSetChanged()
	}
}

class ImageRecyclerViewHolder(itemView: View,
															onItemClick: (String) -> Unit,
															onItemLongClick: (String) -> Unit,
															private val theme: Resources.Theme)
	: BaseEntriesRecyclerViewHolder(itemView) {

	private val imageDisplay: ImageView = itemView.findViewById(R.id.img_image)
	private var imagePath = ""

	init {
		itemView.setOnClickListener {
			onItemClick(imagePath)
		}
		itemView.setOnLongClickListener {
			onItemLongClick(imagePath)
			true
		}
	}

	fun display(imagePath: String) {
		this.imagePath = imagePath
		Glide.with(itemView)
			.load(imagePath)
			.apply(RequestOptions().centerCrop())
			.apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24dp, theme)))
			.into(imageDisplay)
		imageDisplay.imageTintList = null
	}
}
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
package com.marcdonald.hibi.screens.books.mainbooks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R

class BooksRecyclerAdapter(private val context: Context, private val fragmentManager: FragmentManager)
	: RecyclerView.Adapter<BooksFragmentRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var items: List<BookDisplayItem> = listOf()
	private var lastPosition = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksFragmentRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_book, parent, false)
		return BooksFragmentRecyclerViewHolder(view, fragmentManager)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	override fun onBindViewHolder(holder: BooksFragmentRecyclerViewHolder, position: Int) {
		holder.display(items[position])
		setAnimation(holder.itemView, position)
	}

	fun updateList(list: List<BookDisplayItem>) {
		items = list
		notifyDataSetChanged()
	}

	private fun setAnimation(viewToAnimate: View, position: Int) {
		if(position > lastPosition) {
			val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
			viewToAnimate.startAnimation(animation)
			lastPosition = position
		}
	}
}
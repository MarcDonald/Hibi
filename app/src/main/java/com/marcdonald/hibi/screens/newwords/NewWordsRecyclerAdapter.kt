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
package com.marcdonald.hibi.screens.newwords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.entity.NewWord
import timber.log.Timber

class NewWordsRecyclerAdapter(private val context: Context, private val onClick: (Int) -> Unit)
	: RecyclerView.Adapter<NewWordsRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var dataList: List<NewWord> = listOf()
	private var lastPosition = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewWordsRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_new_word, parent, false)
		return NewWordsRecyclerViewHolder(view, onClick)
	}

	override fun getItemCount(): Int {
		return dataList.size
	}

	override fun onBindViewHolder(holder: NewWordsRecyclerViewHolder, position: Int) {
		holder.display(dataList[position])
		setAnimation(holder.itemView, position)
	}

	fun updateList(list: List<NewWord>) {
		dataList = list
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
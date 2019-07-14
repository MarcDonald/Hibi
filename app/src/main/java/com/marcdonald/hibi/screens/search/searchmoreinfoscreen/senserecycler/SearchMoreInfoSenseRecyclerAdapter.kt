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
package com.marcdonald.hibi.screens.search.searchmoreinfoscreen.senserecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Sense

class SearchMoreInfoSenseRecyclerAdapter(context: Context)
	: RecyclerView.Adapter<SearchMoreInfoSenseRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var list: List<Sense> = listOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoreInfoSenseRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_search_result_more_info_sense, parent, false)
		return SearchMoreInfoSenseRecyclerViewHolder(view)
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onBindViewHolder(holder: SearchMoreInfoSenseRecyclerViewHolder, position: Int) {
		holder.display(list[position])
	}

	fun updateList(list: List<Sense>) {
		this.list = list
		notifyDataSetChanged()
	}
}
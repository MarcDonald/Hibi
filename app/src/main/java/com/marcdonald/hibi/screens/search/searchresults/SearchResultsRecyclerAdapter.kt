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
package com.marcdonald.hibi.screens.search.searchresults

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.data.network.jisho.apiresponse.Data

class SearchResultsRecyclerAdapter(context: Context,
																	 private val fragmentManager: FragmentManager)
	: RecyclerView.Adapter<SearchResultsRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var dataList: List<Data> = listOf()
	private var entryId: Int = 0

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_search_result, parent, false)
		return SearchResultsRecyclerViewHolder(view, fragmentManager, entryId)
	}

	override fun getItemCount(): Int {
		return dataList.size
	}

	override fun onBindViewHolder(holder: SearchResultsRecyclerViewHolder, position: Int) {
		holder.display(dataList[position])
	}

	fun updateList(list: List<Data>, entryId: Int) {
		this.entryId = entryId
		dataList = list
		notifyDataSetChanged()
	}
}
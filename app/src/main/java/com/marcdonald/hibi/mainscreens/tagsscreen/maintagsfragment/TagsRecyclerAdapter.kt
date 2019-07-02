package com.marcdonald.hibi.mainscreens.tagsscreen.maintagsfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R

class TagsRecyclerAdapter(private val context: Context,
													private val fragmentManager: FragmentManager)
	: RecyclerView.Adapter<TagsFragmentRecyclerViewHolder>() {

	private val inflater: LayoutInflater = LayoutInflater.from(context)
	private var items: List<TagDisplayItem> = listOf()
	private var lastPosition = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsFragmentRecyclerViewHolder {
		val view = inflater.inflate(R.layout.item_tag, parent, false)
		return TagsFragmentRecyclerViewHolder(view, fragmentManager)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	override fun onBindViewHolder(holder: TagsFragmentRecyclerViewHolder, position: Int) {
		holder.display(items[position])
		setAnimation(holder.itemView, position)
	}

	fun updateList(list: List<TagDisplayItem>) {
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
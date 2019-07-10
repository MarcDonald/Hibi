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
package com.marcdonald.hibi.uicomponents.multiselectdialog

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiDialogFragment

class MultiSelectMenu(private val listener: ItemSelectedListener?) : HibiDialogFragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_select_menu, container, false)
		setupDialog()
		bindViews(view)
		return view
	}

	private fun setupDialog() {
		val layoutParams: WindowManager.LayoutParams? = requireDialog().window?.attributes
		layoutParams?.let {
			layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
			layoutParams.y = 500
		}
	}

	private fun bindViews(view: View) {
		view.findViewById<ImageView>(R.id.img_select_tag).setOnClickListener {
			listener?.itemSelected(TAG)
			dismiss()
		}
		view.findViewById<ImageView>(R.id.img_select_book).setOnClickListener {
			listener?.itemSelected(BOOK)
			dismiss()
		}
		view.findViewById<ImageView>(R.id.img_select_location).setOnClickListener {
			listener?.itemSelected(LOCATION)
			dismiss()
		}
		view.findViewById<ImageView>(R.id.img_select_favourite).setOnClickListener {
			listener?.itemSelected(FAVOURITE)
			dismiss()
		}
		view.findViewById<ImageView>(R.id.img_select_delete).setOnClickListener {
			listener?.itemSelected(DELETE)
			dismiss()
		}
	}

	companion object {
		const val TAG = 0
		const val BOOK = 1
		const val LOCATION = 2
		const val DELETE = 3
		const val FAVOURITE = 4
	}

	interface ItemSelectedListener {
		fun itemSelected(item: Int)
	}
}

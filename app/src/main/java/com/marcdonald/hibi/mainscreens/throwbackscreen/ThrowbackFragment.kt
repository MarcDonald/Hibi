package com.marcdonald.hibi.mainscreens.throwbackscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R

class ThrowbackFragment : Fragment() {
  // <editor-fold desc="UI Components">
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noEntriesDisplay: ConstraintLayout
  private lateinit var recycler: RecyclerView
  // </editor-fold>
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_throwback, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.throwback)
    loadingDisplay = view.findViewById(R.id.const_throwback_loading)
    noEntriesDisplay = view.findViewById(R.id.const_no_throwback)
    recycler = view.findViewById(R.id.recycler_throwback)
  }
}
package com.marcdonald.hibi.mainscreens.throwbackscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.EntriesRecyclerAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ThrowbackFragment : Fragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: ThrowbackFragmentViewModelFactory by instance()
  private lateinit var viewModel: ThrowbackFragmentViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noEntriesDisplay: ConstraintLayout
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(ThrowbackFragmentViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_throwback, container, false)
    bindViews(view)
    initRecycler(view)
    setupObservers()
    viewModel.loadEntries()
    return view
  }

  private fun bindViews(view: View) {
    view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.throwback)
    view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
      Navigation.findNavController(view).popBackStack()
    }
    loadingDisplay = view.findViewById(R.id.const_throwback_loading)
    loadingDisplay.show(false)
    noEntriesDisplay = view.findViewById(R.id.const_no_throwback)
    noEntriesDisplay.show(false)
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.recycler_throwback)
    this.recyclerAdapter = EntriesRecyclerAdapter(requireContext(), requireActivity().theme)
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
    if(includeEntryDividers) {
      val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
      recycler.addItemDecoration(dividerItemDecoration)
    }
  }

  private fun setupObservers() {
    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { shouldShow ->
        loadingDisplay.show(shouldShow)
      }
    })

    viewModel.displayNoResults.observe(this, Observer { value ->
      value?.let { shouldShow ->
        noEntriesDisplay.show(shouldShow)
      }
    })

    viewModel.entries.observe(this, Observer { items ->
      items?.let {
        recyclerAdapter.updateList(items)
      }
    })
  }
}
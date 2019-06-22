package com.marcdonald.hibi.mainscreens.calendarfragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.extension.show
import com.marcdonald.hibi.mainscreens.mainentriesrecycler.EntriesRecyclerAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CalendarFragment : Fragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: CalendarTabViewModelFactory by instance()
  private lateinit var viewModel: CalendarTabViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var calendarView: CalendarView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noResults: ConstraintLayout
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CalendarTabViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_calendar, container, false)

    bindViews(view)
    initRecycler(view)
    setupObservers()
    viewModel.loadData()

    return view
  }

  private fun bindViews(view: View) {
    calendarView = view.findViewById(R.id.calendar_calendarview)
    calendarView.setOnDateChangeListener(calendarViewDateChangeListener)

    loadingDisplay = view.findViewById(R.id.const_calendar_entries_loading)
    loadingDisplay.show(false)

    noResults = view.findViewById(R.id.const_no_calendar_entries)
    noResults.show(false)
  }

  private val calendarViewDateChangeListener = CalendarView.OnDateChangeListener { _, year, month, day ->
    viewModel.loadEntriesForDate(year, month, day)
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.calendar_entries)
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
        noResults.show(shouldShow)
      }
    })

    viewModel.entries.observe(this, Observer { items ->
      items?.let {
        recyclerAdapter.updateList(items)
      }
    })
  }
}
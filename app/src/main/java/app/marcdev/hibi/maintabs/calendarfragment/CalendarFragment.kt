package app.marcdev.hibi.maintabs.calendarfragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_ENTRY_DIVIDERS
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.mainentriesrecycler.EntriesRecyclerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

class CalendarFragment : ScopedFragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: CalendarTabViewModelFactory by instance()
  private lateinit var viewModel: CalendarTabViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var calendarView: CalendarView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter
  // </editor-fold>

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CalendarTabViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_calendar, container, false)

    bindViews(view)
    initRecycler(view)

    launch {
      val c = Calendar.getInstance()
      viewModel.updateList(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
    }

    return view
  }

  private fun bindViews(view: View) {
    calendarView = view.findViewById(R.id.calendar_calendarview)
    calendarView.setOnDateChangeListener(calendarViewDateChangeListener)
  }

  private val calendarViewDateChangeListener = CalendarView.OnDateChangeListener { _, year, month, day ->
    launch {
      viewModel.updateList(year, month, day)
    }
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.calendar_entries)
    this.recyclerAdapter = EntriesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
    if(includeEntryDividers) {
      val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
      recycler.addItemDecoration(dividerItemDecoration)
    }

    displayRecyclerData()
  }

  private fun displayRecyclerData() = launch {
    val displayItems = viewModel.displayItems
    displayItems.observe(this@CalendarFragment, Observer { items ->
      recyclerAdapter.updateList(items)
    })
  }
}
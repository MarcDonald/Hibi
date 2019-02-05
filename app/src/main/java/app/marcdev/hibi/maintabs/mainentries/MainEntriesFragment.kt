package app.marcdev.hibi.maintabs.mainentries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.mainentries.mainentriesrecycler.EntriesRecyclerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class MainEntriesFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: MainEntriesViewModelFactory by instance()
  private lateinit var viewModel: MainEntriesViewModel

  // UI Components
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noResults: ConstraintLayout

  // RecyclerView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainEntriesViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_main_entries, container, false)

    bindViews(view)
    initRecycler(view)

    return view
  }

  private fun bindViews(view: View) {
    loadingDisplay = view.findViewById(R.id.const_entries_loading)
    loadingDisplay.visibility = View.GONE

    noResults = view.findViewById(R.id.const_no_entries)
    noResults.visibility = View.GONE
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.recycler_entries)
    this.recyclerAdapter = EntriesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

//    TODO make this an option
//    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
//    recycler.addItemDecoration(dividerItemDecoration)

    displayRecyclerData()
  }

  private fun displayRecyclerData() = launch {
    loadingDisplay.visibility = View.VISIBLE
    val displayItems = viewModel.displayItems.await()

    displayItems.observe(this@MainEntriesFragment, Observer { items ->
      noResults.visibility = View.GONE

      if(items.isEmpty())
        noResults.visibility = View.VISIBLE
      else
        noResults.visibility = View.GONE

      recyclerAdapter.updateList(items)
      loadingDisplay.visibility = View.GONE
    })
  }
}
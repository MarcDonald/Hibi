package app.marcdev.nichiroku.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.internal.base.ScopedFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class MainScreenFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: MainScreenViewModelFactory by instance()
  private lateinit var viewModel: MainScreenViewModel

  // UI Components
  lateinit var loadingDisplay: ConstraintLayout

  // RecyclerView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainScreenViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_main_screen, container, false)

    bindViews(view)
    initRecycler(view)

    return view
  }

  private fun bindViews(view: View) {
    loadingDisplay = view.findViewById(R.id.const_entries_loading)
    val fab: FloatingActionButton = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabClickListener)
    fab.setOnLongClickListener(fabLongClickListener)
  }

  private val fabClickListener = View.OnClickListener {
    // TODO remove this testing code
    addShortTestEntry()
  }

  private val fabLongClickListener = View.OnLongClickListener {
    // TODO remove this testing code
    addLongTestEntry()
    return@OnLongClickListener true
  }

  private fun addLongTestEntry() = launch {
    viewModel.addLongEntry()
  }

  private fun addShortTestEntry() = launch {
    viewModel.addShortEntry()
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.recycler_entries)
    this.recyclerAdapter = EntriesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
    recycler.addItemDecoration(dividerItemDecoration)

    displayRecyclerData()
  }

  private fun displayRecyclerData() = launch {
    val allEntries = viewModel.allEntries.await()
    allEntries.observe(this@MainScreenFragment, Observer { entries ->
      recyclerAdapter.updateList(entries)

      if(loadingDisplay.visibility == View.VISIBLE) {
        loadingDisplay.visibility = View.GONE
      }
    })
  }
}
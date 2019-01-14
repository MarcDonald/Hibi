package app.marcdev.hibi.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.mainscreen.mainscreenrecycler.EntriesRecyclerAdapter
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
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var fab: FloatingActionButton

  // RecyclerView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainScreenViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_main_screen, container, false)

    bindViews(view)
    initRecycler(view)

    return view
  }

  private fun bindViews(view: View) {
    loadingDisplay = view.findViewById(R.id.const_entries_loading)

    fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabClickListener)

    val scrollView: NestedScrollView = view.findViewById(R.id.scroll_main)
    scrollView.setOnScrollChangeListener(mainOnScrollChangeListener)
  }

  private var mainOnScrollChangeListener = { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int -> hideFabOnScroll(scrollY, oldScrollY) }

  private fun hideFabOnScroll(scrollY: Int, oldScrollY: Int) {
    if(scrollY > oldScrollY) {
      fab.hide()
    } else {
      fab.show()
    }
  }

  private val fabClickListener = View.OnClickListener {
    val addEntryAction = MainScreenFragmentDirections.addEntryAction()
    Navigation.findNavController(it).navigate(addEntryAction)
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
    val displayItems = viewModel.displayItems.await()

    displayItems.observe(this@MainScreenFragment, Observer { items ->
      loadingDisplay.visibility = View.VISIBLE
      recyclerAdapter.updateList(items)
      loadingDisplay.visibility = View.GONE
    })
  }
}
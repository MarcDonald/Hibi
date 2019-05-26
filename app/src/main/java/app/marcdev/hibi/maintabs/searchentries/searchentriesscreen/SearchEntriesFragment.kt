package app.marcdev.hibi.maintabs.searchentries.searchentriesscreen

import android.os.Bundle
import android.preference.PreferenceManager
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_ENTRY_DIVIDERS
import app.marcdev.hibi.maintabs.mainentriesrecycler.EntriesRecyclerAdapter
import app.marcdev.hibi.maintabs.searchentries.EntrySearchCriteria
import app.marcdev.hibi.maintabs.searchentries.SearchCriteriaChangeListener
import app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog.SearchEntriesCriteriaDialog
import com.google.android.material.button.MaterialButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchEntriesFragment : Fragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: SearchEntriesViewModelFactory by instance()
  private lateinit var viewModel: SearchEntriesViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noResults: ConstraintLayout
  private lateinit var toolbarTitle: TextView
  private lateinit var recycler: RecyclerView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter
  private lateinit var searchEntriesCriteriaDialog: SearchEntriesCriteriaDialog
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchEntriesViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_search_entries, container, false)

    bindViews(view)
    initRecycler(view)
    setupObservers()
    viewModel.loadAllEntries()

    return view
  }

  private fun bindViews(view: View) {
    searchEntriesCriteriaDialog = SearchEntriesCriteriaDialog()
    searchEntriesCriteriaDialog.setOnSearchCriteriaChangeListener(criteriaChangeListener)
    loadingDisplay = view.findViewById(R.id.const_search_entries_loading)
    noResults = view.findViewById(R.id.const_no_search_entries_results)
    toolbarTitle = view.findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.search_entries)
    val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
    toolbarBack.setOnClickListener {
      Navigation.findNavController(requireView()).popBackStack()
    }
    val filterButton: MaterialButton = view.findViewById(R.id.fab_search_entries)
    filterButton.setOnClickListener {
      searchEntriesCriteriaDialog.show(requireFragmentManager(), "Search Criteria")
    }
  }

  private val criteriaChangeListener = object : SearchCriteriaChangeListener {
    override fun onSearchCriteriaChange(entrySearchCriteria: EntrySearchCriteria) {
      viewModel.onCriteriaChange(entrySearchCriteria)
    }
  }

  private fun setupObservers() {
    viewModel.entries.observe(this, Observer { value ->
      value?.let { list ->
        recyclerAdapter.updateList(list)
      }
    })

    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { show ->
        loadingDisplay.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayNoResults.observe(this, Observer { value ->
      value?.let { show ->
        if(show) {
          noResults.visibility = View.VISIBLE
          recycler.visibility = View.GONE
        } else {
          noResults.visibility = View.GONE
          recycler.visibility = View.VISIBLE
        }
      }
    })
  }

  private fun initRecycler(view: View) {
    recycler = view.findViewById(R.id.recycler_search_entries)
    recyclerAdapter = EntriesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
    if(includeEntryDividers) {
      val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
      recycler.addItemDecoration(dividerItemDecoration)
    }
  }
}
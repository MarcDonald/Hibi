package app.marcdev.hibi.search.searchresults

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.SEARCH_TERM_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchResultsDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: SearchViewModelFactory by instance()
  private lateinit var viewModel: SearchViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var progressBar: ProgressBar
  private lateinit var noConnectionWarning: LinearLayout
  private lateinit var noResultsWarning: LinearLayout
  private lateinit var recyclerAdapter: SearchResultsRecyclerAdapter
  private lateinit var recycler: RecyclerView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_search, container, false)
    bindViews(view)
    initRecycler()
    setupObservers()
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    arguments?.let {
      val searchTerm = arguments!!.getString(SEARCH_TERM_KEY, "")
      val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(requireView().windowToken, 0)
      viewModel.search(searchTerm)
    }
  }

  private fun bindViews(view: View) {
    recycler = view.findViewById(R.id.recycler_search_results)
    recycler.visibility = View.GONE

    progressBar = view.findViewById(R.id.prog_search_results)
    progressBar.visibility = View.GONE

    noConnectionWarning = view.findViewById(R.id.lin_search_no_connection)
    noConnectionWarning.visibility = View.GONE

    noResultsWarning = view.findViewById(R.id.lin_search_no_results)
    noResultsWarning.visibility = View.GONE
  }

  private fun initRecycler() {
    this.recyclerAdapter = SearchResultsRecyclerAdapter(requireContext(), requireFragmentManager())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
    recycler.addItemDecoration(dividerItemDecoration)
  }

  private fun setupObservers() {
    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { show ->
        progressBar.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayNoConnection.observe(this, Observer { value ->
      value?.let { show ->
        noConnectionWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayNoResults.observe(this, Observer { value ->
      value?.let { show ->
        noResultsWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.searchResults.observe(this, Observer { value ->
      value?.let { searchResult ->
        recyclerAdapter.updateList(searchResult)
        recycler.scrollToPosition(0)
        recycler.visibility = View.VISIBLE
      }
    })
  }
}
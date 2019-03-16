package app.marcdev.hibi.search.searchresults

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.SEARCH_TERM_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchResultsDialog : HibiBottomSheetDialogFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: SearchViewModelFactory by instance()
  private lateinit var viewModel: SearchViewModel

  // UI Components
  private lateinit var progressBar: ProgressBar
  private lateinit var noConnectionWarning: LinearLayout
  private lateinit var noResultsWarning: LinearLayout

  // Recycler
  private lateinit var recyclerAdapter: SearchResultsRecyclerAdapter
  private lateinit var recycler: RecyclerView

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_search, container, false)

    bindViews(view)
    initRecycler()

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val searchTerm = arguments!!.getString(SEARCH_TERM_KEY, "")
      search(searchTerm)
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

  private fun search(searchTerm: String) = launch {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)

    displayLoading(true)

    val response = viewModel.searchTerm(searchTerm)

    if(response == null) {
      displayNoConnection()
    } else {
      if(response.data.isNotEmpty()) {
        recyclerAdapter.updateList(response.data)
        recycler.scrollToPosition(0)
        displayLoading(false)
      } else {
        displayNoResults()
      }
    }
  }

  private fun displayLoading(isLoading: Boolean) {
    if(isLoading) {
      progressBar.visibility = View.VISIBLE
      recycler.visibility = View.GONE
      noConnectionWarning.visibility = View.GONE
    } else {
      progressBar.visibility = View.GONE
      recycler.visibility = View.VISIBLE
    }
  }

  private fun displayNoConnection() {
    progressBar.visibility = View.GONE
    recycler.visibility = View.GONE
    noConnectionWarning.visibility = View.VISIBLE
  }

  private fun displayNoResults() {
    progressBar.visibility = View.GONE
    recycler.visibility = View.GONE
    noResultsWarning.visibility = View.VISIBLE
  }
}
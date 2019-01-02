package app.marcdev.nikki.searchscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.internal.base.ScopedDialogFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchScreenDialog : ScopedDialogFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: SearchScreenViewModelFactory by instance()
  private lateinit var viewModel: SearchScreenViewModel

  // UI Components
  private lateinit var searchBar: EditText
  private lateinit var progressBar: ProgressBar
  private lateinit var noConnectionWarning: LinearLayout
  private lateinit var searchButton: ImageView
  private lateinit var closeButton: ImageView

  // Recycler
  private lateinit var recyclerAdapter: SearchResultsRecyclerAdapter
  private lateinit var recycler: RecyclerView

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchScreenViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_search, container, false)

    bindViews(view)
    initRecycler()

    return view
  }

  private fun bindViews(view: View) {
    recycler = view.findViewById(R.id.recycler_search_results)
    recycler.visibility = View.GONE

    searchBar = view.findViewById(R.id.edt_search_bar)

    progressBar = view.findViewById(R.id.prog_search_results)
    progressBar.visibility = View.GONE

    noConnectionWarning = view.findViewById(R.id.lin_search_no_connection)
    noConnectionWarning.visibility = View.GONE

    searchButton = view.findViewById(R.id.img_search_button)
    searchButton.setOnClickListener(searchClickListener)

    closeButton = view.findViewById(R.id.img_search_close)
    closeButton.setOnClickListener(closeClickListener)
  }

  private fun initRecycler() {
    this.recyclerAdapter = SearchResultsRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
    recycler.addItemDecoration(dividerItemDecoration)
  }

  private val searchClickListener = View.OnClickListener {
    if(searchBar.text.toString().isNotBlank()) {
      search()
    } else {
      Toast.makeText(requireContext(), "Enter something first", Toast.LENGTH_SHORT).show()
    }
  }

  private val closeClickListener = View.OnClickListener {
    searchBar.setText("")
    dismiss()
  }

  private fun search() = launch {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)

    displayLoading(true)

    val response = viewModel.searchTerm(searchBar.text.toString())

    if(response == null) {
      displayLoading(false)
      recycler.visibility = View.GONE
      noConnectionWarning.visibility = View.VISIBLE
    } else {
      if(response.data.isNotEmpty()) {
        recyclerAdapter.updateList(response.data)
        recycler.scrollToPosition(0)
        displayLoading(false)
      }
    }
  }

  private fun displayLoading(isLoading: Boolean) {
    if(isLoading) {
      progressBar.visibility = View.VISIBLE
      recycler.visibility = View.GONE
      searchBar.visibility = View.GONE
      closeButton.visibility = View.GONE
      searchButton.visibility = View.GONE
      noConnectionWarning.visibility = View.GONE
    } else {
      progressBar.visibility = View.GONE
      recycler.visibility = View.VISIBLE
      searchBar.visibility = View.VISIBLE
      closeButton.visibility = View.VISIBLE
      searchButton.visibility = View.VISIBLE
    }
  }
}
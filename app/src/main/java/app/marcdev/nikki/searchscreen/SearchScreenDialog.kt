package app.marcdev.nikki.searchscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
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
  private lateinit var resultsWord: TextView
  private lateinit var resultsReading: TextView
  private lateinit var progressBar: ProgressBar

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchScreenViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_search, container, false)

    bindViews(view)

    return view
  }

  private fun bindViews(view: View) {
    searchBar = view.findViewById(R.id.edt_search_bar)

    progressBar = view.findViewById(R.id.prog_search_results)
    progressBar.visibility = View.GONE

    resultsWord = view.findViewById(R.id.txt_results_word)
    resultsWord.visibility = View.GONE

    resultsReading = view.findViewById(R.id.txt_results_reading)
    resultsReading.visibility = View.GONE

    val searchButton: ImageView = view.findViewById(R.id.img_search_button)
    searchButton.setOnClickListener(searchClickListener)
  }

  private val searchClickListener = View.OnClickListener {
    if(searchBar.text.toString().isNotBlank()) {
      search()
    } else {
      Toast.makeText(requireContext(), "Enter something first", Toast.LENGTH_SHORT).show()
    }
  }

  private fun search() = launch {
    progressBar.visibility = View.VISIBLE
    resultsWord.visibility = View.GONE
    resultsReading.visibility = View.GONE

    val response = viewModel.searchTerm(searchBar.text.toString()).await()

    if(response.data.isNotEmpty()) {
      if(response.data[0].japanese.isNotEmpty()) {
        resultsReading.text = "Reading: ${response.data[0].japanese[0].reading}"
        resultsWord.text = "Word: ${response.data[0].japanese[0].word}"
      }
    } else {
      resultsReading.text = "Null Result"
      resultsWord.text = "Null Result"
    }
    progressBar.visibility = View.GONE
    resultsReading.visibility = View.VISIBLE
    resultsWord.visibility = View.VISIBLE
  }
}
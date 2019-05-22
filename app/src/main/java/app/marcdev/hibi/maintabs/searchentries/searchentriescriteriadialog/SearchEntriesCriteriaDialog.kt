package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import app.marcdev.hibi.maintabs.searchentries.SearchCriteriaChangeListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchEntriesCriteriaDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: SearchEntriesCriteriaDialogViewModelFactory by instance()
  private lateinit var viewModel: SearchEntriesCriteriaDialogViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var beginningDateButton: MaterialButton
  private lateinit var endDateButton: MaterialButton
  private lateinit var contentInput: EditText
  private lateinit var locationInput: EditText
  private lateinit var tagChipGroup: ChipGroup
  private lateinit var bookChipGroup: ChipGroup
  private lateinit var searchButton: MaterialButton
  private lateinit var resetButton: MaterialButton
  // </editor-fold>

  // <editor-fold desc="Other">
  private var _listener = MutableLiveData<SearchCriteriaChangeListener>()
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchEntriesCriteriaDialogViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_entry_search, container, false)
    bindViews(view)
    setupObservers()
    return view
  }

  fun setOnSearchCriteriaChangeListener(searchCriteriaChangeListener: SearchCriteriaChangeListener) {
    _listener.value = searchCriteriaChangeListener
  }

  private fun bindViews(view: View) {
    contentInput = view.findViewById(R.id.edt_search_entries_containing_input)
    locationInput = view.findViewById(R.id.edt_search_entries_location_input)
    tagChipGroup = view.findViewById(R.id.cg_search_entries_tags)
    bookChipGroup = view.findViewById(R.id.cg_search_entries_books)

    beginningDateButton = view.findViewById(R.id.btn_search_entries_beginning)
    beginningDateButton.setOnClickListener {
      // TODO open date picker dialog
      viewModel.setStartDate(1970, 0, 1)
    }

    endDateButton = view.findViewById(R.id.btn_search_entries_end)
    endDateButton.setOnClickListener {
      // TODO open time picker dialog
      viewModel.setEndDate(2018, 7, 28)
    }

    searchButton = view.findViewById(R.id.btn_search_entries_go)
    searchButton.setOnClickListener { viewModel.search() }

    resetButton = view.findViewById(R.id.btn_search_entries_reset)
    resetButton.setOnClickListener { viewModel.reset(true) }
  }

  private fun setupObservers() {
    viewModel.beginningDisplay.observe(this, Observer { value ->
      value?.let { beginningText ->
        if(beginningText.isBlank())
          beginningDateButton.text = resources.getString(R.string.start)
        else
          beginningDateButton.text = beginningText
      }
    })

    viewModel.endDisplay.observe(this, Observer { value ->
      value?.let { endText ->
        if(endText.isBlank())
          endDateButton.text = resources.getString(R.string.finish)
        else
          endDateButton.text = endText
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })

    _listener.observe(this, Observer { value ->
      value?.let { listener ->
        viewModel.setCriteriaChangeListener(listener)
      }
    })
  }
}
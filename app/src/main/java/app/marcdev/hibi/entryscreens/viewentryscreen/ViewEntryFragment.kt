package app.marcdev.hibi.entryscreens.viewentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.IS_EDIT_MODE_KEY
import app.marcdev.hibi.internal.base.BinaryOptionDialog
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import app.marcdev.hibi.search.searchresults.SearchResultsDialog
import app.marcdev.hibi.uicomponents.newwordsdialog.NewWordDialog
import app.marcdev.hibi.uicomponents.views.SearchBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class ViewEntryFragment : ScopedFragment(), KodeinAware {
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: ViewEntryViewModelFactory by instance()
  private lateinit var viewModel: ViewEntryViewModel

  // UI Components
  private lateinit var dateButton: MaterialButton
  private lateinit var timeButton: MaterialButton
  private lateinit var contentDisplay: TextView
  private lateinit var deleteConfirmDialog: BinaryOptionDialog
  private lateinit var searchBar: SearchBar
  private lateinit var tagDisplay: ChipGroup
  private lateinit var tagDisplayHolder: LinearLayout
  private lateinit var newWordsButton: MaterialButton

  // Other
  private var entryIdBeingViewed = 0

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_view_entry, container, false)

    bindViews(view)
    initDeleteConfirmDialog()

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val entryId = ViewEntryFragmentArgs.fromBundle(it).entryId
      if(entryId == 0) {
        Toast.makeText(requireContext(), resources.getString(R.string.generic_error), Toast.LENGTH_SHORT).show()
        Timber.e("Log: onViewCreated: View Entry With Id 0")
      } else {
        fillData(entryId)
      }
    }
  }

  private fun fillData(entryId: Int) = launch {
    if(viewModel.hasNewWords(entryId))
      newWordsButton.visibility = View.VISIBLE
    else
      newWordsButton.visibility = View.GONE

    entryIdBeingViewed = entryId
    viewModel.getEntry(entryId).observe(this@ViewEntryFragment, Observer { entry ->
      contentDisplay.text = entry.content

      val day = entry.day
      val month = entry.month
      val year = entry.year
      val hour = entry.hour
      val minute = entry.minute

      dateButton.text = formatDateForDisplay(day, month, year)
      timeButton.text = formatTimeForDisplay(hour, minute)
    })

    viewModel.getTags(entryId).observe(this@ViewEntryFragment, Observer { tags ->
      tagDisplay.removeAllViews()

      if(tags.isEmpty()) {
        tagDisplayHolder.visibility = View.GONE
      } else {
        tagDisplayHolder.visibility = View.VISIBLE
        tags.forEach {
          val displayTag = Chip(tagDisplay.context)
          displayTag.text = it
          tagDisplay.addView(displayTag)
        }
      }
    })
  }

  private fun bindViews(view: View) {
    dateButton = view.findViewById(R.id.btn_view_date)
    timeButton = view.findViewById(R.id.btn_view_time)
    contentDisplay = view.findViewById(R.id.txt_view_content)
    tagDisplay = view.findViewById(R.id.cg_view_tags)
    tagDisplayHolder = view.findViewById(R.id.lin_view_tags)
    searchBar = view.findViewById(R.id.searchbar_view_entry)
    searchBar.setSearchAction(this::search)

    val backButton: ImageView = view.findViewById(R.id.img_view_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)

    val editButton: ImageView = view.findViewById(R.id.img_edit)
    editButton.setOnClickListener(editClickListener)

    val deleteButton: ImageView = view.findViewById(R.id.img_delete)
    deleteButton.setOnClickListener(deleteClickListener)

    newWordsButton = view.findViewById(R.id.btn_view_new_words)
    newWordsButton.setOnClickListener(newWordsClickListener)
  }

  private val backClickListener = View.OnClickListener {
    Navigation.findNavController(view!!).popBackStack()
  }

  private val editClickListener = View.OnClickListener {
    val editEntryAction = ViewEntryFragmentDirections.editEntryAction()
    if(entryIdBeingViewed != 0) {
      editEntryAction.entryId = entryIdBeingViewed
    }
    Navigation.findNavController(it).navigate(editEntryAction)
  }

  private val deleteClickListener = View.OnClickListener {
    deleteConfirmDialog.show(requireFragmentManager(), "Delete Confirmation Dialog")
  }

  private val newWordsClickListener = View.OnClickListener {
    val dialog = NewWordDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, entryIdBeingViewed)
    bundle.putBoolean(IS_EDIT_MODE_KEY, false)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "View New Words Dialog")
  }

  private fun search(searchTerm: String) {
    val args = Bundle()
    args.putString("searchTerm", searchTerm)

    val searchDialog = SearchResultsDialog()
    searchDialog.arguments = args

    searchDialog.show(requireFragmentManager(), "View Entry Search")
  }

  private fun initDeleteConfirmDialog() {
    deleteConfirmDialog = BinaryOptionDialog()
    deleteConfirmDialog.setTitle(resources.getString(R.string.delete_confirm_title))
    deleteConfirmDialog.setMessage(resources.getString(R.string.delete_confirm))
    deleteConfirmDialog.setNegativeButton(resources.getString(R.string.delete), okDeleteClickListener)
    deleteConfirmDialog.setPositiveButton(resources.getString(R.string.cancel), cancelDeleteClickListener)
  }

  private val okDeleteClickListener = View.OnClickListener {
    deleteEntry()
    deleteConfirmDialog.dismiss()
  }

  private val cancelDeleteClickListener = View.OnClickListener {
    deleteConfirmDialog.dismiss()
  }

  private fun deleteEntry() = launch {
    viewModel.deleteEntry(entryIdBeingViewed)
    Navigation.findNavController(requireView()).popBackStack()
  }
}
package app.marcdev.nikki.viewentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.nikki.R
import app.marcdev.nikki.internal.base.ScopedFragment
import app.marcdev.nikki.internal.formatDateForDisplay
import app.marcdev.nikki.internal.formatTimeForDisplay
import app.marcdev.nikki.uicomponents.TransparentSquareButton
import app.marcdev.nikki.uicomponents.YesNoDialog
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
  lateinit var dateButton: TransparentSquareButton
  lateinit var timeButton: TransparentSquareButton
  lateinit var contentDisplay: TextView
  lateinit var deleteConfirmDialog: YesNoDialog

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
    entryIdBeingViewed = entryId
    viewModel.getEntry(entryId).observe(this@ViewEntryFragment, Observer { entry ->
      contentDisplay.text = entry.content

      val day = entry.day
      val month = entry.month
      val year = entry.year
      val hour = entry.hour
      val minute = entry.minute

      dateButton.setText(formatDateForDisplay(day, month, year))
      timeButton.setText(formatTimeForDisplay(hour, minute))
    })
  }

  private fun bindViews(view: View) {
    dateButton = view.findViewById(R.id.btn_view_date)
    timeButton = view.findViewById(R.id.btn_view_time)
    contentDisplay = view.findViewById(R.id.txt_view_content)

    val backButton: ImageView = view.findViewById(R.id.img_view_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)

    val editButton: ImageView = view.findViewById(R.id.img_edit)
    editButton.setOnClickListener(editClickListener)

    val deleteButton: ImageView = view.findViewById(R.id.img_delete)
    deleteButton.setOnClickListener(deleteClickListener)
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

  private fun initDeleteConfirmDialog() {
    deleteConfirmDialog = YesNoDialog()
    deleteConfirmDialog.setTitle(resources.getString(R.string.delete_confirm_title))
    deleteConfirmDialog.setMessage(resources.getString(R.string.delete_confirm))
    deleteConfirmDialog.setYesButton(resources.getString(R.string.delete), okDeleteClickListener)
    deleteConfirmDialog.setNoButton(resources.getString(R.string.cancel), cancelDeleteClickListener)
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
    Navigation.findNavController(view!!).popBackStack()
  }
}
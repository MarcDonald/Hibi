package app.marcdev.nikki.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import app.marcdev.nikki.searchscreen.SearchScreenDialog
import app.marcdev.nikki.uicomponents.TransparentSquareButton
import app.marcdev.nikki.uicomponents.YesNoDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddEntryFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: AddEntryViewModelFactory by instance()
  private lateinit var viewModel: AddEntryViewModel

  // UI Components
  private lateinit var dateButton: TransparentSquareButton
  private lateinit var timeButton: TransparentSquareButton
  private lateinit var contentInput: EditText
  private lateinit var backConfirmDialog: YesNoDialog
  private lateinit var searchDialog: SearchScreenDialog
  private lateinit var toolbarTitle: TextView

  // Other
  private val dateTimeStore = DateTimeStore()
  private var entryIdBeingEdited = 0

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_add_entry, container, false)

    bindViews(view)
    initBackConfirmDialog()
    initSearchDialog()

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val entryId = AddEntryFragmentArgs.fromBundle(it).entryId
      if(entryId != 0) {
        convertToEditMode(entryId)
      }
    }
  }

  private fun bindViews(view: View) {
    toolbarTitle = view.findViewById(R.id.txt_add_toolbar_title)
    dateButton = view.findViewById(R.id.btn_date)
    dateButton.setOnClickListener(dateClickListener)
    initDateButton()

    timeButton = view.findViewById(R.id.btn_time)
    timeButton.setOnClickListener(timeClickListener)
    initTimeButton()

    contentInput = view.findViewById(R.id.edt_content)

    val saveButton: TransparentSquareButton = view.findViewById(R.id.btn_save)
    saveButton.setOnClickListener(saveClickListener)

    val backButton: ImageView = view.findViewById(R.id.img_add_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)

    val fab: FloatingActionButton = view.findViewById(R.id.fab_add_entry_search)
    fab.setOnClickListener(fabClickListener)
  }

  private fun initBackConfirmDialog() {
    backConfirmDialog = YesNoDialog()
    backConfirmDialog.setTitle(resources.getString(R.string.warning_caps))
    backConfirmDialog.setMessage(resources.getString(R.string.go_back_warning))
    backConfirmDialog.setYesButton(resources.getString(R.string.ok), okBackClickListener)
    backConfirmDialog.setNoButton(resources.getString(R.string.cancel), cancelBackClickListener)
  }

  private fun initSearchDialog() {
    searchDialog = SearchScreenDialog()
  }

  private val saveClickListener = View.OnClickListener {
    onSaveClick()
  }

  private val backClickListener = View.OnClickListener {
    if(contentInput.text.toString().isBlank()) {
      popBackStack()
    } else {
      backConfirmDialog.show(requireFragmentManager(), "Back Confirm Dialog")
    }
  }

  private val okBackClickListener = View.OnClickListener {
    backConfirmDialog.dismiss()
    popBackStack()
  }

  private val cancelBackClickListener = View.OnClickListener {
    backConfirmDialog.dismiss()
  }

  private val dateClickListener = View.OnClickListener {
    val dateDialog = DatePickerDialog()
    dateDialog.bindDateTimeStore(dateTimeStore)
    dateDialog.show(requireFragmentManager(), "Date Picker")
  }

  private val timeClickListener = View.OnClickListener {
    val timeDialog = TimePickerDialog()
    timeDialog.bindDateTimeStore(dateTimeStore)
    timeDialog.show(requireFragmentManager(), "Time Picker")
  }

  private val fabClickListener = View.OnClickListener {
    searchDialog.show(requireFragmentManager(), "Add Entry Search")
  }

  private fun initDateButton() {
    dateTimeStore.readableDate.observe(this@AddEntryFragment, Observer { date ->
      dateButton.setText(date)
    })
  }

  private fun initTimeButton() {
    dateTimeStore.readableTime.observe(this@AddEntryFragment, Observer { time ->
      timeButton.setText(time)
    })
  }

  private fun convertToEditMode(entryId: Int) = launch {
    toolbarTitle.text = resources.getString(R.string.edit_entry)
    entryIdBeingEdited = entryId
    viewModel.getEntry(entryId).observe(this@AddEntryFragment, Observer { entry ->
      contentInput.setText(entry.content)

      val day = entry.day
      val month = entry.month
      val year = entry.year
      val hour = entry.hour
      val minute = entry.minute

      dateTimeStore.setDate(day, month, year)
      dateTimeStore.setTime(hour, minute)
      dateButton.setText(formatDateForDisplay(day, month, year))
      timeButton.setText(formatTimeForDisplay(hour, minute))
    })
  }

  private fun onSaveClick() = launch {
    val content = contentInput.text.toString()

    if(content.isBlank()) {
      Toast.makeText(requireContext(), resources.getString(R.string.empty_content_warning), Toast.LENGTH_SHORT).show()
    } else {
      val day = dateTimeStore.getDay()
      val month = dateTimeStore.getMonth()
      val year = dateTimeStore.getYear()
      val hour = dateTimeStore.getHour()
      val minute = dateTimeStore.getMinute()

      if(entryIdBeingEdited == 0) {
        viewModel.addEntry(day, month, year, hour, minute, content)
      } else {
        viewModel.updateEntry(day, month, year, hour, minute, content, entryIdBeingEdited)
      }

      popBackStack()
    }
  }

  private fun popBackStack() {
    Navigation.findNavController(view!!).popBackStack()
  }
}

package app.marcdev.nichiroku.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.internal.base.ScopedFragment
import app.marcdev.nichiroku.uicomponents.YesNoDialog
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
  private lateinit var dateButton: FrameLayout
  private lateinit var timeButton: FrameLayout
  private lateinit var contentInput: EditText
  private lateinit var backConfirmDialog: YesNoDialog

  // Other
  private val dateTimeStore = DateTimeStore()

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_add_entry, container, false)

    bindViews(view)
    initBackConfirmDialog()

    return view
  }

  private fun bindViews(view: View) {
    dateButton = view.findViewById(R.id.frame_date)
    dateButton.setOnClickListener(dateClickListener)
    initDateButton()

    timeButton = view.findViewById(R.id.frame_time)
    timeButton.setOnClickListener(timeClickListener)
    initTimeButton()

    contentInput = view.findViewById(R.id.edt_content)

    val saveButton: FrameLayout = view.findViewById(R.id.frame_save)
    saveButton.setOnClickListener(saveClickListener)

    val backButton: ImageView = view.findViewById(R.id.img_add_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)
  }

  private fun initBackConfirmDialog() {
    backConfirmDialog = YesNoDialog()
    backConfirmDialog.setTitle(resources.getString(R.string.warning_caps))
    backConfirmDialog.setMessage(resources.getString(R.string.go_back_warning))
    backConfirmDialog.setYesButton(resources.getString(R.string.ok), okBackClickListener)
    backConfirmDialog.setNoButton(resources.getString(R.string.cancel), cancelBackClickListener)
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

  private fun initDateButton() {
    dateTimeStore.readableDate.observe(this@AddEntryFragment, Observer { date ->
      val dateDisplay: TextView = dateButton.findViewById(R.id.txt_date)
      dateDisplay.text = date
    })
  }

  private fun initTimeButton() {
    dateTimeStore.readableTime.observe(this@AddEntryFragment, Observer { time ->
      val timeDisplay: TextView = timeButton.findViewById(R.id.txt_time)
      timeDisplay.text = time
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
      viewModel.addEntry(day, month, year, hour, minute, content)
      popBackStack()
    }
  }

  private fun popBackStack() {
    Navigation.findNavController(view!!).popBackStack()
  }
}

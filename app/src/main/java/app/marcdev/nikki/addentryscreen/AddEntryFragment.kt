package app.marcdev.nikki.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.nikki.R
import app.marcdev.nikki.internal.base.ScopedFragment
import app.marcdev.nikki.uicomponents.TransparentSquareButton
import app.marcdev.nikki.uicomponents.YesNoDialog
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
      dateButton.setText(date)
    })
  }

  private fun initTimeButton() {
    dateTimeStore.readableTime.observe(this@AddEntryFragment, Observer { time ->
      timeButton.setText(time)
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
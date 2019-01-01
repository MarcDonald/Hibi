package app.marcdev.nichiroku.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.internal.base.ScopedFragment
import com.google.android.material.button.MaterialButton
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

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save)
    saveButton.setOnClickListener(saveClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    onSaveClick()
    Navigation.findNavController(it).popBackStack()
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
    val day = dateTimeStore.getDay()
    val month = dateTimeStore.getMonth()
    val year = dateTimeStore.getYear()
    val hour = dateTimeStore.getHour()
    val minute = dateTimeStore.getMinute()
    val content = contentInput.text.toString()
    viewModel.addEntry(day, month, year, hour, minute, content)
  }
}

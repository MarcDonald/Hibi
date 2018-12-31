package app.marcdev.nichiroku.addentryscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.internal.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddEntryFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: AddEntryViewModelFactory by instance()
  private lateinit var viewModel: AddEntryViewModel

  // UI Components
  private lateinit var dateInput: EditText
  private lateinit var timeInput: EditText
  private lateinit var contentInput: EditText

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_add_entry, container, false)

    bindViews(view)

    return view
  }

  private fun bindViews(view: View) {
    dateInput = view.findViewById(R.id.edt_date)
    timeInput = view.findViewById(R.id.edt_time)
    contentInput = view.findViewById(R.id.edt_content)
    val saveButton: Button = view.findViewById(R.id.btn_save)
    saveButton.setOnClickListener(saveClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    onSaveClick()
    Navigation.findNavController(it).popBackStack()
  }

  private fun onSaveClick() = launch {
    val date = dateInput.text.toString()
    val time = timeInput.text.toString()
    val content = contentInput.text.toString()
    viewModel.addEntry(date, time, content)
  }
}

package app.marcdev.hibi.uicomponents.locationdialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddLocationToEntryDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddLocationToEntryViewModelFactory by instance()
  private lateinit var viewModel: AddLocationToEntryViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var input: EditText
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddLocationToEntryViewModel::class.java)
    arguments?.let {
      viewModel.passArgument(requireArguments().getInt(ENTRY_ID_KEY, 0))
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_entry_location, container, false)
    bindViews(view)
    setupObservers()
    return view
  }

  private fun bindViews(view: View) {
    input = view.findViewById(R.id.edt_add_location_input)
    input.setOnKeyListener(saveOnEnterListener)

    val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_location)
    deleteButton.setOnClickListener {
      viewModel.delete()
    }

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_location)
    saveButton.setOnClickListener {
      viewModel.save(input.text.toString())
    }

    input.requestFocus()
  }

  private fun setupObservers() {
    viewModel.currentLocation.observe(this, Observer { value ->
      value?.let { currentLocation ->
        input.setText(currentLocation)
      }
    })

    viewModel.displayEmptyError.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          input.error = resources.getString(R.string.empty_content_warning)
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })
  }

  private val saveOnEnterListener: View.OnKeyListener =
    View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
      if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
        viewModel.save(input.text.toString())
      }
      /* This is false so that the event isn't consumed and other buttons (such as the back button)
       * can be pressed */
      false
    }
}
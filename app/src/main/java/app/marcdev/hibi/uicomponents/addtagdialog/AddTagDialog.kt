package app.marcdev.hibi.uicomponents.addtagdialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddTagDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var input: EditText

  // Viewmodel
  private val viewModelFactory: AddTagViewModelFactory by instance()
  private lateinit var viewModel: AddTagViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_new_tag, container, false)
    bindViews(view)
    input.requestFocus()
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTagViewModel::class.java)
  }

  private fun bindViews(view: View) {
    input = view.findViewById(R.id.edt_new_tag_input)
    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_tags)
    saveButton.setOnClickListener(saveClickListener)
    input.setOnKeyListener(saveOnEnterListener)
  }

  private val saveClickListener = View.OnClickListener {
    saveTag()
  }

  private val saveOnEnterListener: View.OnKeyListener =
    View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
      if ((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
        saveTag()
      }
      true
    }

  private fun saveTag() = launch {
    val save = viewModel.addTag(input.text.toString())
    if(!save) {
      input.error = resources.getString(R.string.empty_content_warning)
    } else {
      dismiss()
    }
  }
}
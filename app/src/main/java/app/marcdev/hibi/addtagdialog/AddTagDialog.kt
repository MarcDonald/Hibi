package app.marcdev.hibi.addtagdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.uicomponents.HibiDialogFragment
import app.marcdev.hibi.uicomponents.TransparentSquareButton
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
    val saveButton: TransparentSquareButton = view.findViewById(R.id.btn_save_tags)
    saveButton.setOnClickListener(saveClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    saveTag()
  }

  private fun saveTag() = launch {
    val save = viewModel.addTag(input.text.toString())
    if(!save) {
      Toast.makeText(requireContext(), resources.getString(R.string.empty_content_warning), Toast.LENGTH_SHORT).show()
    } else {
      dismiss()
    }
  }
}
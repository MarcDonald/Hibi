package app.marcdev.hibi.uicomponents.addtagdialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.TAG_ID_KEY
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

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddTagViewModelFactory by instance()
  private lateinit var viewModel: AddTagViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var input: EditText
  private lateinit var title: TextView
  // </editor-fold>

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

    arguments?.let {
      val tagId = arguments!!.getInt(TAG_ID_KEY, 0)
      viewModel.tagId = tagId
      if(tagId != 0)
        title.text = resources.getString(R.string.edit_tag)

      launch {
        input.setText(viewModel.getTagName())
      }
    }
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_tag_title)
    input = view.findViewById(R.id.edt_new_tag_input)
    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_tag)
    saveButton.setOnClickListener(saveClickListener)
    val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_tag)
    deleteButton.setOnClickListener(deleteClickListener)
    input.setOnKeyListener(saveOnEnterListener)
    input.requestFocus()
  }

  private val saveClickListener = View.OnClickListener {
    saveTag()
  }

  private val deleteClickListener = View.OnClickListener {
    launch {
      viewModel.deleteTag()
    }
    dismiss()
  }

  private val saveOnEnterListener: View.OnKeyListener =
    View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
      if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
        saveTag()
      }
      /* This is false so that the event isn't consumed and other buttons (such as the back button)
       * can be pressed */
      false
    }

  private fun saveTag() = launch {
    if(input.text.toString().isBlank())
      input.error = resources.getString(R.string.empty_content_warning)
    else {
      val save = viewModel.addTag(input.text.toString())
      if(!save) {
        input.error = resources.getString(R.string.tag_already_exists)
      } else {
        dismiss()
      }
    }
  }
}
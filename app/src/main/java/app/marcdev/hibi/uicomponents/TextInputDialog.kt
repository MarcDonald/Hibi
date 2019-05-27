package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber

class TextInputDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="UI Components">
  private lateinit var input: EditText
  private lateinit var dialogTitle: TextView
  private lateinit var saveButton: MaterialButton
  private lateinit var deleteButton: MaterialButton
  // </editor-fold>

  // <editor-fold desc="To Set">
  private var saveClickListenerToSet: TextInputDialogSaveListener? = null
  private var deleteClickListenerToSet: View.OnClickListener? = null
  private var _contentToSet: String = ""
  private var _hintToSet: String = ""
  private var _titleToSet: String = ""
  // </editor-fold>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_text_input, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    dialogTitle = view.findViewById(R.id.txt_text_input_title)
    if(_titleToSet.isNotBlank())
      dialogTitle.text = _titleToSet

    input = view.findViewById(R.id.edt_text_input_input)
    if(_contentToSet.isNotBlank())
      input.setText(_contentToSet)
    if(_hintToSet.isNotBlank())
      input.hint = _hintToSet

    input.requestFocus()

    saveButton = view.findViewById(R.id.btn_text_input_save)
    saveButton.setOnClickListener {
      saveClickListenerToSet?.onSave(input.text.toString())
    }

    deleteButton = view.findViewById(R.id.btn_text_input_delete)
    deleteButton.setOnClickListener(deleteClickListenerToSet)
  }

  private fun setSaveClickListener(listener: TextInputDialogSaveListener?) {
    saveClickListenerToSet = listener
  }

  private fun setDeleteClickListener(listener: View.OnClickListener?) {
    deleteClickListenerToSet = listener
  }

  private fun setContent(contentArg: String) {
    _contentToSet = contentArg
  }

  private fun setHint(hintArg: String) {
    _hintToSet = hintArg
  }

  private fun setTitle(titleArg: String) {
    _titleToSet = titleArg
  }

  class Builder {
    private var _deleteClickListener: View.OnClickListener? = null
    private var _saveClickListener: TextInputDialogSaveListener? = null
    private var _content: String = ""
    private var _hint: String = ""
    private var _title: String = ""

    fun setSaveClickListener(listener: TextInputDialogSaveListener?): Builder {
      _saveClickListener = listener
      return this
    }

    fun setDeleteClickListener(listener: View.OnClickListener?): Builder {
      _deleteClickListener = listener
      return this
    }

    fun setContent(contentArg: String): Builder {
      _content = contentArg
      return this
    }

    fun setHint(hintArg: String): Builder {
      _hint = hintArg
      return this
    }

    fun setTitle(titleArg: String): Builder {
      _title = titleArg
      return this
    }

    fun build(): TextInputDialog {
      val dialog = TextInputDialog()
      dialog.setSaveClickListener(_saveClickListener)
      dialog.setDeleteClickListener(_deleteClickListener)
      dialog.setContent(_content)
      dialog.setHint(_hint)
      dialog.setTitle(_title)
      return dialog
    }
  }

  interface TextInputDialogSaveListener {
    fun onSave(text: String)
  }
}
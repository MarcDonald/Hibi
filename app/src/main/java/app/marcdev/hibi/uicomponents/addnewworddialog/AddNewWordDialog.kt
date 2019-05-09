package app.marcdev.hibi.uicomponents.addnewworddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.NEW_WORD_ID_KEY
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddNewWordDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()
  // <editor-fold desc="View Model">
  private val viewModelFactory: AddNewWordViewModelFactory by instance()
  private lateinit var viewModel: AddNewWordViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var dialogTitle: TextView
  private lateinit var wordInput: EditText
  private lateinit var readingInput: EditText
  private lateinit var typeInput: EditText
  private lateinit var englishInput: EditText
  private lateinit var notesInput: EditText
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddNewWordViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_add_new_word, container, false)
    bindViews(view)
    setupObservers()
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    arguments?.let {
      viewModel.passArguments(arguments!!.getInt(ENTRY_ID_KEY, 0), arguments!!.getInt(NEW_WORD_ID_KEY, 0))
    }
  }

  private fun bindViews(view: View) {
    wordInput = view.findViewById(R.id.edt_new_word_word)
    readingInput = view.findViewById(R.id.edt_new_word_reading)
    typeInput = view.findViewById(R.id.edt_new_word_type)
    englishInput = view.findViewById(R.id.edt_new_word_english)
    notesInput = view.findViewById(R.id.edt_new_word_notes)
    dialogTitle = view.findViewById(R.id.txt_add_new_word_title)

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_new_word)
    saveButton.setOnClickListener {
      viewModel.saveNewWord(wordInput.text.toString(), readingInput.text.toString(), typeInput.text.toString(), englishInput.text.toString(), notesInput.text.toString())
    }

    val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_new_word)
    deleteButton.setOnClickListener {
      viewModel.deleteNewWord()
    }
  }

  private fun setupObservers() {
    viewModel.word.observe(this, Observer { value ->
      value?.let { newWord ->
        wordInput.setText(newWord.word)
        readingInput.setText(newWord.reading)
        typeInput.setText(newWord.partOfSpeech)
        englishInput.setText(newWord.english)
        notesInput.setText(newWord.notes)
      }
    })

    viewModel.isEditMode.observe(this, Observer { value ->
      value?.let { isEditMode ->
        if(isEditMode)
          dialogTitle.text = resources.getString(R.string.edit_new_word)
      }
    })

    viewModel.displayEmptyInputWarning.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          wordInput.error = resources.getString(R.string.empty_input_new_word)
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })
  }
}
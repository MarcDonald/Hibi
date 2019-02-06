package app.marcdev.hibi.newwordsdialog.addnewworddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.NEW_WORD_ID_KEY
import app.marcdev.hibi.uicomponents.HibiDialogFragment
import app.marcdev.hibi.uicomponents.TransparentSquareButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddNewWordDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var wordInput: EditText
  private lateinit var readingInput: EditText
  private lateinit var typeInput: EditText
  private lateinit var englishInput: EditText
  private lateinit var notesInput: EditText

  // Viewmodel
  private val viewModelFactory: AddNewWordViewModelFactory by instance()
  private lateinit var viewModel: AddNewWordViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_add_new_word, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddNewWordViewModel::class.java)

    arguments?.let {
      val entryId = arguments!!.getInt(ENTRY_ID_KEY, 0)
      viewModel.entryId = entryId

      val newWordId = arguments!!.getInt(NEW_WORD_ID_KEY, 0)
      if(newWordId != 0) {
        viewModel.newWordId = newWordId
        fillData()
      }
    }
  }

  private fun bindViews(view: View) {
    wordInput = view.findViewById(R.id.edt_new_word_word)
    readingInput = view.findViewById(R.id.edt_new_word_reading)
    typeInput = view.findViewById(R.id.edt_new_word_type)
    englishInput = view.findViewById(R.id.edt_new_word_english)
    notesInput = view.findViewById(R.id.edt_new_word_notes)

    val saveButton: TransparentSquareButton = view.findViewById(R.id.btn_save_new_word)
    saveButton.setOnClickListener(saveClickListener)

    val deleteButton: TransparentSquareButton = view.findViewById(R.id.btn_delete_new_word)
    deleteButton.setOnClickListener(deleteClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    if(wordInput.text.isBlank() && readingInput.text.isBlank()) {
      Toast.makeText(requireContext(), resources.getString(R.string.empty_content_warning), Toast.LENGTH_SHORT).show()
    } else {
      launch {
        viewModel.saveNewWord(wordInput.text.toString(), readingInput.text.toString(), typeInput.text.toString(), englishInput.text.toString(), notesInput.text.toString())
        dismiss()
      }
    }
  }

  private val deleteClickListener = View.OnClickListener {
    if(viewModel.newWordId == 0) {
      dismiss()
    } else {
      launch {
        viewModel.deleteNewWord()
        dismiss()
      }
    }
  }

  private fun fillData() = launch {
    val entry = viewModel.getNewWord()
    wordInput.setText(entry.word)
    readingInput.setText(entry.reading)
    typeInput.setText(entry.partOfSpeech)
    englishInput.setText(entry.english)
    notesInput.setText(entry.notes)
  }
}
package app.marcdev.hibi.uicomponents.addbookdialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.BOOK_ID_KEY
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddBookDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var input: EditText
  private lateinit var title: TextView

  // Viewmodel
  private val viewModelFactory: AddBookViewModelFactory by instance()
  private lateinit var viewModel: AddBookViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_new_book, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddBookViewModel::class.java)

    arguments?.let {
      val bookId = arguments!!.getInt(BOOK_ID_KEY, 0)
      viewModel.bookId = bookId
      if(bookId != 0)
        title.text = resources.getString(R.string.edit_book)

      launch {
        input.setText(viewModel.getBookName())
      }
    }
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_book_title)
    input = view.findViewById(R.id.edt_new_book_input)
    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_book)
    saveButton.setOnClickListener(saveClickListener)
    val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_book)
    deleteButton.setOnClickListener(deleteClickListener)
    input.setOnKeyListener(saveOnEnterListener)
    input.requestFocus()
  }

  private val saveClickListener = View.OnClickListener {
    saveBook()
  }

  private val deleteClickListener = View.OnClickListener {
    launch {
      viewModel.deleteBook()
    }
    dismiss()
  }

  private val saveOnEnterListener: View.OnKeyListener =
    View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
      if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
        saveBook()
      }
      /* This is false so that the event isn't consumed and other buttons (such as the back button)
       * can be pressed */
      false
    }

  private fun saveBook() = launch {
    if(input.text.toString().isBlank())
      input.error = resources.getString(R.string.empty_content_warning)
    else {
      val save = viewModel.addBook(input.text.toString())
      if(!save) {
        input.error = resources.getString(R.string.book_already_exists)
      } else {
        dismiss()
      }
    }
  }
}
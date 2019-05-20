package app.marcdev.hibi.uicomponents.addentrytobookdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addbookdialog.AddBookDialog
import app.marcdev.hibi.uicomponents.views.CheckBoxWithId
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddEntryToBookDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddEntryToBookViewModelFactory by instance()
  private lateinit var viewModel: AddEntryToBookViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var title: TextView
  private lateinit var bookHolder: LinearLayout
  private lateinit var noBooksWarning: TextView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryToBookViewModel::class.java)
    arguments?.let {
      viewModel.passArguments(arguments!!.getInt(ENTRY_ID_KEY, 0))
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_entry_books, container, false)
    bindViews(view)
    setupObservers()
    return view
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_entry_to_book_title)
    bookHolder = view.findViewById(R.id.lin_add_entry_to_book_book_holder)
    noBooksWarning = view.findViewById(R.id.txt_no_books_warning)

    val addButton: MaterialButton = view.findViewById(R.id.btn_create_book)
    addButton.setOnClickListener {
      val dialog = AddBookDialog()
      dialog.show(requireFragmentManager(), "Book Manager Dialog")
    }

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_book)
    saveButton.setOnClickListener(saveClickListener)
  }

  private fun setupObservers() {
    viewModel.allBooks.observe(this, Observer { value ->
      value?.let { books ->
        viewModel.listReceived(books.isEmpty())

        books.forEach { book ->
          // Gets list of all books currently displayed
          val alreadyDisplayedBooks = ArrayList<CheckBoxWithId>()
          for(x in 0 until bookHolder.childCount) {
            val bookCheckBox = bookHolder.getChildAt(x) as CheckBoxWithId
            alreadyDisplayedBooks.add(bookCheckBox)
          }

          val displayBook = CheckBoxWithId(bookHolder.context)
          displayBook.text = book.name
          displayBook.itemId = book.id
          if(theme == R.style.Hibi_DarkTheme_BottomSheetDialogTheme) {
            displayBook.setTextColor(resources.getColor(R.color.darkThemePrimaryText, null))
          } else {
            displayBook.setTextColor(resources.getColor(R.color.lightThemePrimaryText, null))
          }

          // If the new book is already displayed, don't add it
          // This stops it removing user progress before saving
          var addIt = true
          alreadyDisplayedBooks.forEach { alreadyDisplayedBook ->
            if(alreadyDisplayedBook.itemId == displayBook.itemId) {
              addIt = false
            }
          }

          if(addIt)
            bookHolder.addView(displayBook)
        }
      }
    })

    viewModel.displayNoBookWarning.observe(this, Observer { value ->
      value?.let { show ->
        noBooksWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.bookEntryRelations.observe(this, Observer { value ->
      value?.let { bookEntryRelations ->
        bookEntryRelations.forEach { bookId ->
          for(x in 0 until bookHolder.childCount) {
            val bookCheckBox = bookHolder.getChildAt(x) as CheckBoxWithId
            if(bookCheckBox.itemId == bookId)
              bookCheckBox.isChecked = true
          }
        }
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })
  }

  private val saveClickListener = View.OnClickListener {
    val list = arrayListOf<Pair<Int, Boolean>>()
    for(x in 0 until bookHolder.childCount) {
      val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
      list.add(Pair(checkBox.itemId, checkBox.isChecked))
    }
    viewModel.onSaveClick(list)
  }
}
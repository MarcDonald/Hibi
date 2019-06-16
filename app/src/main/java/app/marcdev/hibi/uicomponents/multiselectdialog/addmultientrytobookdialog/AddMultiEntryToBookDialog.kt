package app.marcdev.hibi.uicomponents.multiselectdialog.addmultientrytobookdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addbookdialog.AddBookDialog
import app.marcdev.hibi.uicomponents.views.CheckBoxWithId
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddMultiEntryToBookDialog(private val selectedCount: Int, private val onSaveClick: (Boolean, List<Int>) -> Unit) : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddMultiEntryToBookViewModelFactory by instance()
  private lateinit var viewModel: AddMultiEntryToBookViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var title: TextView
  private lateinit var bookHolder: LinearLayout
  private lateinit var noBooksWarning: TextView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddMultiEntryToBookViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_multi_entry_books, container, false)
    bindViews(view)
    setupObservers()
    return view
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_books_multi_title)
    title.text = resources.getQuantityString(R.plurals.multi_book_title, selectedCount, selectedCount)
    bookHolder = view.findViewById(R.id.lin_books_entry_multi_books_holder)
    noBooksWarning = view.findViewById(R.id.txt_no_books_multi_warning)

    val addButton: MaterialButton = view.findViewById(R.id.btn_add_book_multi)
    addButton.setOnClickListener {
      val dialog = AddBookDialog()
      dialog.show(requireFragmentManager(), "Book Manager Dialog")
    }

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_books_multi)
    saveButton.setOnClickListener(saveClickListener)

    val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_books_multi)
    deleteButton.setOnClickListener(deleteClickListener)

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
          if(theme == R.style.Theme_Hibi_BottomSheetDialog_Dark) {
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

    viewModel.displayNoBooksWarning.observe(this, Observer { value ->
      value?.let { show ->
        noBooksWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })
  }

  private val saveClickListener = View.OnClickListener {
    val list = mutableListOf<Int>()
    for(x in 0 until bookHolder.childCount) {
      val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
      if(checkBox.isChecked)
        list.add(checkBox.itemId)
    }
    onSaveClick(false, list)
    dismiss()
  }

  private val deleteClickListener = View.OnClickListener {
    val list = mutableListOf<Int>()
    for(x in 0 until bookHolder.childCount) {
      val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
      if(checkBox.isChecked)
        list.add(checkBox.itemId)
    }
    onSaveClick(true, list)
    dismiss()
  }
}
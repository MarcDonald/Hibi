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
import app.marcdev.hibi.entryscreens.addentryscreen.TagsToSaveToNewEntry
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addtagdialog.AddTagDialog
import app.marcdev.hibi.uicomponents.views.CheckBoxWithId
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddEntryToBookDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var title: TextView
  private lateinit var bookHolder: LinearLayout
  private lateinit var noTagsWarning: TextView

  // Viewmodel
  private val viewModelFactory: AddEntryToBookViewModelFactory by instance()
  private lateinit var viewModel: AddEntryToBookViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_entry_books, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryToBookViewModel::class.java)

    arguments?.let {
      val entryId = arguments!!.getInt(ENTRY_ID_KEY)
      viewModel.entryId = entryId
    }

    displayTags()
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_entry_to_book_title)
    bookHolder = view.findViewById(R.id.lin_add_entry_to_book_book_holder)
    noTagsWarning = view.findViewById(R.id.txt_no_books_warning)

    val addButton: MaterialButton = view.findViewById(R.id.btn_create_book)
    addButton.setOnClickListener(addClickListener)

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_book)
    saveButton.setOnClickListener(saveClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    for(x in 0 until bookHolder.childCount) {
      val checkBox = bookHolder.getChildAt(x) as CheckBoxWithId
      if(checkBox.isChecked) {
        save(checkBox.itemId)
      } else {
        delete(checkBox.itemId)
      }
    }
    dismiss()
  }

  private fun save(tagId: Int) = launch {
    viewModel.saveBookEntryRelation(tagId)
  }

  private fun delete(tag: Int) = launch {
    viewModel.deleteBookEntryRelation(tag)
  }

  private val addClickListener = View.OnClickListener {
    val dialog = AddTagDialog()
    dialog.show(requireFragmentManager(), "Book Manager Dialog")
  }

  private fun displayTags() = launch {
    val tagEntryRelations = if(viewModel.entryId == 0) {
      TagsToSaveToNewEntry.list
    } else {
      viewModel.getBooksForEntry()
    }

    val allTags = viewModel.allBooks.await()
    allTags.observe(this@AddEntryToBookDialog, Observer { books ->
      books.forEach { it ->
        // Gets list of all tags displayed
        val alreadyDisplayedTags = ArrayList<CheckBoxWithId>()
        for(x in 0 until bookHolder.childCount) {
          val tagCheckBox = bookHolder.getChildAt(x) as CheckBoxWithId
          alreadyDisplayedTags.add(tagCheckBox)
        }

        val displayTag = CheckBoxWithId(bookHolder.context)
        displayTag.text = it.name
        displayTag.itemId = it.id
        if(theme == R.style.Hibi_DarkTheme_BottomSheetDialogTheme) {
          displayTag.setTextColor(resources.getColor(R.color.darkThemePrimaryText, null))
        } else {
          displayTag.setTextColor(resources.getColor(R.color.lightThemePrimaryText, null))
        }

        // If the new tag is already displayed, don't add it
        // This stops it removing user progress before saving
        var addIt = true
        alreadyDisplayedTags.forEach {
          if(it.text == displayTag.text) {
            addIt = false
          }
        }

        if(addIt) {
          if(tagEntryRelations.contains(it.id)) {
            displayTag.isChecked = true
          }
          bookHolder.addView(displayTag)
        }
      }

      if(bookHolder.childCount == 0) {
        noTagsWarning.visibility = View.VISIBLE
      } else {
        noTagsWarning.visibility = View.GONE
      }
    })
  }
}
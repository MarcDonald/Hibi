package app.marcdev.hibi.uicomponents.newwordsdialog

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.data.entity.NewWord
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.NEW_WORD_ID_KEY
import app.marcdev.hibi.uicomponents.addnewworddialog.AddNewWordDialog

class NewWordsRecyclerViewHolder(itemView: View,
                                 fragmentManager: FragmentManager,
                                 isEditMode: Boolean)
  : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_new_word_word)
  private val typeDisplay: TextView = itemView.findViewById(R.id.txt_new_word_part)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_new_word_reading)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_new_word_english)
  private val notesDisplay: TextView = itemView.findViewById(R.id.txt_new_word_notes)
  private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.const_new_word)
  private val addNewWordDialog: AddNewWordDialog = AddNewWordDialog()

  private var displayedNewWord: NewWord? = null

  private val clickListener = View.OnClickListener {
    val arguments = Bundle()
    arguments.putInt(ENTRY_ID_KEY, displayedNewWord!!.entryId)
    arguments.putInt(NEW_WORD_ID_KEY, displayedNewWord!!.id)
    addNewWordDialog.arguments = arguments

    addNewWordDialog.show(fragmentManager, "Edit Word Dialog")
  }

  init {
    if(isEditMode)
      constraintLayout.setOnClickListener(clickListener)
  }

  fun display(newWord: NewWord) {
    displayedNewWord = newWord

    if(newWord.word.isNotBlank()) {
      wordDisplay.text = itemView.resources.getString(R.string.japanese_word_wc, newWord.word)
    }
    else
      wordDisplay.visibility = View.GONE

    if(newWord.partOfSpeech.isNotBlank())
      typeDisplay.text = newWord.partOfSpeech
    else
      typeDisplay.visibility = View.GONE

    if(newWord.reading.isNotBlank()) {
      readingDisplay.text = itemView.resources.getString(R.string.reading_wc, newWord.reading)
    }
    else
      readingDisplay.visibility = View.GONE

    if(newWord.english.isNotBlank())
      englishDisplay.text = newWord.english
    else
      englishDisplay.visibility = View.GONE

    if(newWord.notes.isNotBlank()) {
      notesDisplay.text = itemView.resources.getString(R.string.notes_wc, newWord.notes)
    }
    else
      notesDisplay.visibility = View.GONE
  }
}
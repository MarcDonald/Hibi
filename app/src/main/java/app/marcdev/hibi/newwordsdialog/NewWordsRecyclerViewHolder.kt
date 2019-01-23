package app.marcdev.hibi.newwordsdialog

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.data.entity.NewWord

class NewWordsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_new_word_word)
  private val typeDisplay: TextView = itemView.findViewById(R.id.txt_new_word_part)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_new_word_reading)
  private val englishDisplay: TextView = itemView.findViewById(R.id.txt_new_word_english)
  private val notesDisplay: TextView = itemView.findViewById(R.id.txt_new_word_notes)
  private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.const_new_word)

  private var displayedNewWord: NewWord? = null

  fun display(newWord: NewWord) {
    displayedNewWord = newWord

    wordDisplay.text = newWord.word
    typeDisplay.text = newWord.partOfSpeech
    readingDisplay.text = newWord.reading
    englishDisplay.text = newWord.english
    notesDisplay.text = newWord.notes

    // TODO logic for when to display certain ones
  }
}
package app.marcdev.nikki.searchmoreinfoscreen.alternativesrecycler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nikki.R
import app.marcdev.nikki.data.network.apiresponse.Japanese

class SearchMoreInfoAlternativesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val wordDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_word)
  private val readingDisplay: TextView = itemView.findViewById(R.id.txt_search_more_info_alternative_reading)

  private var displayedJapanese: Japanese? = null
  private var wordContent = ""
  private var readingContent = ""

  private val wordClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Alternative Word", wordContent)
    clipboard.primaryClip = clip

    val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard, wordContent)
    Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
  }

  private val readingClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Alternative Reading", readingContent)
    clipboard.primaryClip = clip

    val toastMessage = itemView.resources.getString(R.string.copied_to_clipboard, readingContent)
    Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
  }

  init {
    wordDisplay.setOnClickListener(wordClickListener)
    readingDisplay.setOnClickListener(readingClickListener)
  }

  fun display(japanese: Japanese) {
    displayedJapanese = japanese
    displayJapanese(japanese)
  }

  private fun displayJapanese(japanese: Japanese) {
    val word: String? = japanese.word
    val reading: String? = japanese.reading

    if(reading != null) {
      if(word == null || word.isBlank()) {
        wordDisplay.text = itemView.resources.getString(R.string.search_results_word, reading)
        readingDisplay.visibility = View.GONE
        wordContent = reading
      } else {
        wordDisplay.text = itemView.resources.getString(R.string.search_results_word, word)
        wordContent = word
        if(reading.isBlank()) {
          readingDisplay.visibility = View.GONE
        } else {
          readingDisplay.text = itemView.resources.getString(R.string.search_results_reading, reading)
          readingContent = reading
        }
      }
    }
  }
}
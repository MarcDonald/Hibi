package app.marcdev.nikki.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.marcdev.nikki.R

class YesNoDialog : DialogFragment() {

  // UI Components
  lateinit var yesButton: TransparentSquareButton
  lateinit var noButton: TransparentSquareButton
  lateinit var titleDisplay: TextView
  lateinit var messageDisplay: TextView

  // To set
  private var yesText = ""
  private var noText = ""
  private var titleText = ""
  private var messageText = ""
  private var yesClickListener: View.OnClickListener? = null
  private var noClickListener: View.OnClickListener? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_yes_no, container, false)
    bindViews(view)
    setContent()
    return view
  }

  private fun bindViews(view: View) {
    yesButton = view.findViewById(R.id.btn_yes_no_positive)
    noButton = view.findViewById(R.id.btn_yes_no_negative)

    titleDisplay = view.findViewById(R.id.txt_yes_no_title)
    messageDisplay = view.findViewById(R.id.txt_yes_no_message)
  }

  private fun setContent() {
    if(titleText.isNotBlank()) {
      titleDisplay.text = titleText
    }

    if(messageText.isNotBlank()) {
      messageDisplay.text = messageText
    }

    if(yesText.isNotBlank()) {
      yesButton.setText(yesText)
    }

    if(noText.isNotBlank()) {
      noButton.setText(noText)
    }

    if(yesClickListener != null) {
      yesButton.setOnClickListener(yesClickListener)
    } else {
      yesButton.setOnClickListener(defaultClickListener)
    }

    if(noClickListener != null) {
      noButton.setOnClickListener(noClickListener)
    } else {
      noButton.setOnClickListener(defaultClickListener)
    }
  }

  fun setYesButton(text: String, clickListener: View.OnClickListener) {
    yesText = text
    yesClickListener = clickListener
  }

  fun setNoButton(text: String, clickListener: View.OnClickListener) {
    noText = text
    noClickListener = clickListener
  }

  fun setTitle(text: String) {
    if(view != null) {
      titleDisplay.text = text
    } else {
      titleText = text
    }
  }

  fun setMessage(text: String) {
    if(view != null) {
      messageDisplay.text = text
    } else {
      messageText = text
    }
  }

  private val defaultClickListener = View.OnClickListener {
    dismiss()
  }
}
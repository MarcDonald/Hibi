package app.marcdev.hibi.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.marcdev.hibi.R

class BinaryOptionDialog : HibiDialogFragment() {

  // UI Components
  private lateinit var rightButton: TransparentSquareButton
  private lateinit var leftButton: TransparentSquareButton
  private lateinit var titleDisplay: TextView
  private lateinit var messageDisplay: TextView

  // To set
  private var rightButtonText = ""
  private var leftButtonText = ""
  private var titleText = ""
  private var messageText = ""
  private var rightButtonClickListener: View.OnClickListener? = null
  private var leftButtonClickListener: View.OnClickListener? = null
  private var isTitleVisible = true
  private var isMessageVisible = true

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_binary_option, container, false)
    bindViews(view)
    setContent()
    return view
  }

  private fun bindViews(view: View) {
    rightButton = view.findViewById(R.id.btn_yes_no_positive)
    leftButton = view.findViewById(R.id.btn_yes_no_negative)

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

    if(rightButtonText.isNotBlank()) {
      rightButton.setText(rightButtonText)
    }

    if(leftButtonText.isNotBlank()) {
      leftButton.setText(leftButtonText)
    }

    if(rightButtonClickListener != null) {
      rightButton.setOnClickListener(rightButtonClickListener)
    } else {
      rightButton.setOnClickListener(defaultClickListener)
    }

    if(leftButtonClickListener != null) {
      leftButton.setOnClickListener(leftButtonClickListener)
    } else {
      leftButton.setOnClickListener(defaultClickListener)
    }

    if(isTitleVisible)
      titleDisplay.visibility = View.VISIBLE
    else
      titleDisplay.visibility = View.GONE

    if(isMessageVisible)
      messageDisplay.visibility = View.VISIBLE
    else
      messageDisplay.visibility = View.GONE
  }

  fun setRightButton(text: String, clickListener: View.OnClickListener) {
    rightButtonText = text
    rightButtonClickListener = clickListener
  }

  fun setLeftButton(text: String, clickListener: View.OnClickListener) {
    leftButtonText = text
    leftButtonClickListener = clickListener
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

  fun setTitleVisibility(isVisible: Boolean) {
    isTitleVisible = isVisible
  }

  fun setMessageVisiblity(isVisible: Boolean) {
    isMessageVisible = isVisible
  }

  private val defaultClickListener = View.OnClickListener {
    dismiss()
  }
}
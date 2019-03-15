package app.marcdev.hibi.uicomponents.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import app.marcdev.hibi.R

class SearchBar(context: Context, attributeSet: AttributeSet?, defStyle: Int) : ConstraintLayout(context, attributeSet, defStyle) {

  private var editText: EditText
  private var searchButton: ImageView

  init {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.view_search_bar, this, true)
    editText = view.findViewById(R.id.edt_search_bar)
    searchButton = view.findViewById(R.id.img_search_button)

    if(attributeSet != null) {
      val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SearchBar, defStyle, 0)

      val hint = attributes.getString(R.styleable.SearchBar_hint)
      editText.hint = hint

      attributes.recycle()
    }
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  fun setHint(hint: String) {
    editText.hint = hint
  }

  fun setSearchAction(searchAction: (searchTerm: String) -> Unit) {
    val buttonClickListener = OnClickListener {
      val input = editText.text.toString()
      if(input.isNotBlank()) {
        searchAction(input)
        editText.setText("")
      } else {
        Toast.makeText(context, context.resources.getString(R.string.no_search_term_warning), Toast.LENGTH_SHORT).show()
      }
    }
    searchButton.setOnClickListener(buttonClickListener)

    val searchOnEnterListener: View.OnKeyListener = View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
      if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
        val input = editText.text.toString()
        if(input.isNotBlank()) {
          searchAction(input)
          editText.setText("")
        } else {
          Toast.makeText(context, context.resources.getString(R.string.no_search_term_warning), Toast.LENGTH_SHORT).show()
        }
      }
      false
    }
    editText.setOnKeyListener(searchOnEnterListener)
  }
}
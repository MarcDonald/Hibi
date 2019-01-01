package app.marcdev.nikki.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import app.marcdev.nikki.R

class TransparentSquareButton(context: Context, attributeSet: AttributeSet?, defStyle: Int) : FrameLayout(context, attributeSet, defStyle) {

  private var textView: TextView
  private var frame: FrameLayout

  init {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.view_transparent_square_button, this, true)
    textView = view.findViewById(R.id.txt_transparent_square_button)
    frame = view.findViewById(R.id.frame_transparent_square_button)

    if(attributeSet != null) {
      val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TransparentSquareButton, defStyle, 0)

      val text = attributes.getString(R.styleable.TransparentSquareButton_text)
      textView.text = text

      val borderlessRipple = attributes.getBoolean(R.styleable.TransparentSquareButton_borderlessRipple, false)
      if(borderlessRipple) {
        setSelectableItemBackgroundBorderless(borderlessRipple)
      }

      attributes.recycle()
    }
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  fun setText(text: String) {
    textView.text = text
  }

  fun setSelectableItemBackgroundBorderless(borderless: Boolean) {
    val attrs = IntArray(1)
    if(borderless) {
      attrs[0] = android.R.attr.selectableItemBackgroundBorderless
    } else {
      attrs[0] = android.R.attr.selectableItemBackground
    }

    val typedArray = context.obtainStyledAttributes(attrs)
    val drawable = typedArray.getDrawable(0)
    typedArray.recycle()
    frame.background = drawable
  }

  override fun setOnClickListener(l: OnClickListener?) {
    frame.setOnClickListener(l)
  }
}
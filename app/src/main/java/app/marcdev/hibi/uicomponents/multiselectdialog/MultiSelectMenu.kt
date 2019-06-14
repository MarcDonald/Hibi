package app.marcdev.hibi.uicomponents.multiselectdialog

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment

class MultiSelectMenu(private val listener: ItemSelectedListener?) : HibiDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_select_menu, container, false)
    setupDialog()
    bindViews(view)
    return view
  }

  private fun setupDialog() {
    val layoutParams: WindowManager.LayoutParams? = requireDialog().window?.attributes
    if(layoutParams != null) {
      layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
      layoutParams.y = 500
    }
  }

  private fun bindViews(view: View) {
    view.findViewById<ImageView>(R.id.img_select_tag).setOnClickListener {
      listener?.itemSelected(TAG)
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_book).setOnClickListener {
      listener?.itemSelected(BOOK)
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_location).setOnClickListener {
      listener?.itemSelected(LOCATION)
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_delete).setOnClickListener {
      listener?.itemSelected(DELETE)
      dismiss()
    }
  }

  companion object {
    const val TAG = 0
    const val BOOK = 1
    const val LOCATION = 2
    const val DELETE = 3
  }

  interface ItemSelectedListener {
    fun itemSelected(item: Int)
  }
}

package app.marcdev.hibi.uicomponents.multiselectdialog

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment

class MultiSelectMenu(private val idList: List<Int>) : HibiDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_select_menu, container, false)
    setupDialog()
    bindViews(view)
    return view
  }

  private fun setupDialog() {
    requireDialog().window?.setDimAmount(0f)
    val layoutParams: WindowManager.LayoutParams? = requireDialog().window?.attributes
    if(layoutParams != null) {
      layoutParams.gravity = Gravity.BOTTOM or Gravity.END
      layoutParams.x = 16
      layoutParams.y = 160
    }
  }

  private fun bindViews(view: View) {
    view.findViewById<ImageView>(R.id.img_select_tag).setOnClickListener {
      // TODO
      Toast.makeText(requireContext(), "Tag $idList", Toast.LENGTH_SHORT).show()
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_book).setOnClickListener {
      // TODO
      Toast.makeText(requireContext(), "Book $idList", Toast.LENGTH_SHORT).show()
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_location).setOnClickListener {
      // TODO
      Toast.makeText(requireContext(), "Location $idList", Toast.LENGTH_SHORT).show()
      dismiss()
    }
    view.findViewById<ImageView>(R.id.img_select_delete).setOnClickListener {
      // TODO
      Toast.makeText(requireContext(), "Delete $idList", Toast.LENGTH_SHORT).show()
      dismiss()
    }
  }
}

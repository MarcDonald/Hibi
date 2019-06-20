package app.marcdev.hibi.mainscreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class MainScreenMenuDialog : HibiBottomSheetDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_main_menu, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    val settingsButton: MaterialButton = view.findViewById(R.id.btn_main_menu_settings)
    settingsButton.setOnClickListener(settingsClickListener)

    val aboutButton: MaterialButton = view.findViewById(R.id.btn_main_menu_about)
    aboutButton.setOnClickListener(aboutClickListener)
  }

  private val settingsClickListener = View.OnClickListener {
    val settingsAction = MainScreenFragmentDirections.settingsAction()
    Navigation.findNavController(requireParentFragment().requireView()).navigate(settingsAction)
    dismiss()
  }

  private val aboutClickListener = View.OnClickListener {
    val aboutAction = MainScreenFragmentDirections.aboutAction()
    Navigation.findNavController(requireParentFragment().requireView()).navigate(aboutAction)
    dismiss()
  }
}
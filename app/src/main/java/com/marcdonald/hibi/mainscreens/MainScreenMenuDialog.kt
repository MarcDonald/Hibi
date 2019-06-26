package com.marcdonald.hibi.mainscreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiBottomSheetDialogFragment

class MainScreenMenuDialog : HibiBottomSheetDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_main_menu, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    view.findViewById<MaterialButton>(R.id.btn_main_menu_throwback)
      .setOnClickListener {
        val throwbackAction = MainScreenFragmentDirections.throwbackAction()
        Navigation.findNavController(requireParentFragment().requireView()).navigate(throwbackAction)
        dismiss()
      }

    view.findViewById<MaterialButton>(R.id.btn_main_menu_settings)
      .setOnClickListener {
        val settingsAction = MainScreenFragmentDirections.settingsAction()
        Navigation.findNavController(requireParentFragment().requireView()).navigate(settingsAction)
        dismiss()
      }

    view.findViewById<MaterialButton>(R.id.btn_main_menu_about)
      .setOnClickListener {
        val aboutAction = MainScreenFragmentDirections.aboutAction()
        Navigation.findNavController(requireParentFragment().requireView()).navigate(aboutAction)
        dismiss()
      }
  }
}
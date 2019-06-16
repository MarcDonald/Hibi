package app.marcdev.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class HibiBottomSheetDialogFragment : BottomSheetDialogFragment() {
  override fun getTheme(): Int {
    return if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_DARK_THEME, false))
      R.style.Theme_Hibi_BottomSheetDialog_Dark
    else
      R.style.Theme_Hibi_BottomSheetDialog_Light
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}
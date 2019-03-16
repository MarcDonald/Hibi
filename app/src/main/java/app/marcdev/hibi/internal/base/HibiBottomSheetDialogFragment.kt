package app.marcdev.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class HibiBottomSheetDialogFragment : ScopedBottomSheetDialogFragment() {
  override fun getTheme(): Int {
    if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_DARK_THEME, false))
      return R.style.Hibi_DarkTheme_BottomSheetDialogTheme
    else
      return R.style.Hibi_LightTheme_BottomSheetDialogTheme
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}
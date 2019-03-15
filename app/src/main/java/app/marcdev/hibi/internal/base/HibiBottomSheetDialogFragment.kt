package app.marcdev.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import app.marcdev.hibi.R
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class HibiBottomSheetDialogFragment : ScopedBottomSheetDialogFragment() {
  override fun getTheme(): Int {
    return R.style.Hibi_LightTheme_BottomSheetDialogTheme
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}
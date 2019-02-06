package app.marcdev.hibi.uicomponents

import android.app.Dialog
import android.os.Bundle
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class HibiBottomSheetDialogFragment : ScopedBottomSheetDialogFragment() {
  override fun getTheme(): Int {
    return R.style.Hibi_LightTheme_BottomSheetDialogTheme
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}
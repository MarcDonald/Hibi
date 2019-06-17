package app.marcdev.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.utils.ThemeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class HibiBottomSheetDialogFragment : BottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()
  private val themeUtils: ThemeUtils by instance()

  override fun getTheme(): Int {
    return if(themeUtils.isDarkMode())
      R.style.Theme_Hibi_BottomSheetDialog_Dark
    else
      R.style.Theme_Hibi_BottomSheetDialog_Light
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}
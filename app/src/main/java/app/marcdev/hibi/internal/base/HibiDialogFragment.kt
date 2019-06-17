package app.marcdev.hibi.internal.base

import androidx.fragment.app.DialogFragment
import app.marcdev.hibi.R

abstract class HibiDialogFragment : DialogFragment() {
  override fun onStart() {
    super.onStart()
    requireDialog().window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
  }
}
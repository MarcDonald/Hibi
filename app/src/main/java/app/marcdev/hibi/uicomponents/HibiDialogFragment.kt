package app.marcdev.hibi.uicomponents

import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedDialogFragment

abstract class HibiDialogFragment : ScopedDialogFragment() {
  override fun onStart() {
    super.onStart()
    requireDialog().window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
  }
}
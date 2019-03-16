package app.marcdev.hibi.internal.base

import app.marcdev.hibi.R

abstract class HibiDialogFragment : ScopedDialogFragment() {
  override fun onStart() {
    super.onStart()
    requireDialog().window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
  }
}
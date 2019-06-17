package app.marcdev.hibi.maintabs.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton

class PrivacyDialog : HibiDialogFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_privacy, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    val dismissButton: MaterialButton = view.findViewById(R.id.btn_privacy_dismiss)
    dismissButton.setOnClickListener { dismiss() }

    val jishoButton: MaterialButton = view.findViewById(R.id.btn_privacy_jisho)
    jishoButton.setOnClickListener { openJisho() }
  }

  private fun openJisho() {
    val uriUrl = Uri.parse("https://jisho.org/about")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
  }
}
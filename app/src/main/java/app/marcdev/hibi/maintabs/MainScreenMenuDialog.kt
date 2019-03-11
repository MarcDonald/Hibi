package app.marcdev.hibi.maintabs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.marcdev.hibi.R
import app.marcdev.hibi.uicomponents.HibiBottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class MainScreenMenuDialog : HibiBottomSheetDialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_main_menu, container, false)

    bindViews(view)

    return view
  }

  private fun bindViews(view: View) {
    val settingsButton: MaterialButton = view.findViewById(R.id.btn_settings)
    settingsButton.setOnClickListener(settingsClickListener)

    val openSourceButton: MaterialButton = view.findViewById(R.id.btn_oss_licences)
    openSourceButton.setOnClickListener(openSourceClickListener)

    val codeButton: MaterialButton = view.findViewById(R.id.btn_source_code)
    codeButton.setOnClickListener(codeClickListener)
  }

  private val settingsClickListener = View.OnClickListener {
    Toast.makeText(requireContext(), resources.getString(R.string.settings), Toast.LENGTH_SHORT).show()
    dismiss()
  }

  private val openSourceClickListener = View.OnClickListener {
    Toast.makeText(requireContext(), resources.getString(R.string.open_source_licences), Toast.LENGTH_SHORT).show()
    dismiss()
  }

  private val codeClickListener = View.OnClickListener {
    val uriUrl = Uri.parse("https://github.com/MarcDonald/Hibi")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
    dismiss()
  }
}
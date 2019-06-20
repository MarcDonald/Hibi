package app.marcdev.hibi.mainscreens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.uicomponents.views.LicenseDisplay

class OpenSourceLicencesFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_oss, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.open_source_licenses)
    view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener(backClickListener)

    view.findViewById<LicenseDisplay>(R.id.license_timber)
      .setOnClickListener(openURLClickListener("https://github.com/JakeWharton/timber"))

    view.findViewById<LicenseDisplay>(R.id.license_kodein)
      .setOnClickListener(openURLClickListener("https://github.com/Kodein-Framework/Kodein-DI"))

    view.findViewById<LicenseDisplay>(R.id.license_retrofit)
      .setOnClickListener(openURLClickListener("https://github.com/square/retrofit"))

    view.findViewById<LicenseDisplay>(R.id.license_gson)
      .setOnClickListener(openURLClickListener("https://github.com/google/gson"))

    view.findViewById<LicenseDisplay>(R.id.license_android_file_picker)
      .setOnClickListener(openURLClickListener("https://github.com/DroidNinja/Android-FilePicker"))

    view.findViewById<LicenseDisplay>(R.id.license_mplus)
      .setOnClickListener(openURLClickListener("https://fonts.google.com/specimen/M+PLUS+Rounded+1c"))

    view.findViewById<LicenseDisplay>(R.id.license_open_sans)
      .setOnClickListener(openURLClickListener("https://fonts.google.com/specimen/Open+Sans"))

    view.findViewById<LicenseDisplay>(R.id.license_google_material_icons)
      .setOnClickListener(openURLClickListener("https://material.io/tools/icons/"))

    view.findViewById<LicenseDisplay>(R.id.license_material_icons)
      .setOnClickListener(openURLClickListener("https://materialdesignicons.com/"))

    view.findViewById<LicenseDisplay>(R.id.license_glide)
      .setOnClickListener(openURLClickListener("https://github.com/bumptech/glide"))
  }

  private val backClickListener = View.OnClickListener {
    Navigation.findNavController(requireView()).popBackStack()
  }

  private fun openURLClickListener(url: String): View.OnClickListener {
    return View.OnClickListener {
      val uriUrl = Uri.parse(url)
      val launchBrowser = Intent(Intent.ACTION_VIEW)
      launchBrowser.data = uriUrl
      startActivity(launchBrowser)
    }
  }
}
package app.marcdev.hibi.mainscreens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import app.marcdev.hibi.R

class SettingsFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_settings, container, false)
    view.findViewById<TextView>(R.id.txt_back_toolbar_title).text = resources.getString(R.string.settings)
    view.findViewById<ImageView>(R.id.img_back_toolbar_back).setOnClickListener {
      Navigation.findNavController(view).popBackStack()
    }
    return view
  }
}
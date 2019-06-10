package app.marcdev.hibi.uicomponents.views

import android.content.Context
import android.preference.PreferenceManager
import android.widget.CheckBox
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_DARK_THEME

class CheckBoxWithId(context: Context) : CheckBox(context) {
  var itemId: Int = 0

  init {
    if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_DARK_THEME, false))
      buttonTintList = resources.getColorStateList(R.color.state_list_checkbox_dark, null)
  }
}
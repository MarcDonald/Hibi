package app.marcdev.hibi.maintabs.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiActivity

class SettingsActivity : HibiActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_empty_scroll_with_toolbar)
    bindViews()

    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.scroll_empty, SettingsFragment())
    fragmentTransaction.commit()
  }

  private fun bindViews() {
    val toolbarTitle: TextView = findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.settings)

    val toolbarBackButton: ImageView = findViewById(R.id.img_back_toolbar_back)
    toolbarBackButton.setOnClickListener(backClickListener)
  }

  private val backClickListener = View.OnClickListener {
    onBackPressed()
  }
}
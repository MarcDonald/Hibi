package app.marcdev.hibi.maintabs.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.hibi.R
import timber.log.Timber

class AboutActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)

    bindViews()

    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.scroll_about, AboutFragment())
    fragmentTransaction.commit()
  }

  private fun bindViews() {
    val toolbarTitle: TextView = findViewById(R.id.txt_back_toolbar_title)
    toolbarTitle.text = resources.getString(R.string.about)

    val toolbarBackButton: ImageView = findViewById(R.id.img_back_toolbar_back)
    toolbarBackButton.setOnClickListener(backClickListener)
  }

  private val backClickListener = View.OnClickListener {
    onBackPressed()
  }
}
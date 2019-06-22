package com.marcdonald.hibi.mainscreens.settings.updatedialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import com.marcdonald.hibi.internal.extension.show
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class UpdateDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: UpdateDialogViewModelFactory by instance()
  private lateinit var viewModel: UpdateDialogViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var noUpdateAvailable: ImageView
  private lateinit var errorDisplay: ImageView
  private lateinit var updateAvailable: ImageView
  private lateinit var noConnection: LinearLayout
  private lateinit var loadingProgressBar: ProgressBar
  private lateinit var openButton: MaterialButton
  private lateinit var dismissButton: MaterialButton
  private lateinit var title: TextView
  private lateinit var message: TextView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateDialogViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_update, container, false)
    bindViews(view)
    setupObservers()
    viewModel.check()
    return view
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_update_title)
    message = view.findViewById(R.id.txt_update_message)
    message.show(false)
    noConnection = view.findViewById(R.id.lin_update_no_connection)
    noUpdateAvailable = view.findViewById(R.id.img_no_update_available)
    updateAvailable = view.findViewById(R.id.img_update_available)
    updateAvailable.show(false)
    errorDisplay = view.findViewById(R.id.img_update_error)
    errorDisplay.show(false)
    loadingProgressBar = view.findViewById(R.id.prog_update)
    dismissButton = view.findViewById(R.id.btn_update_dismiss)
    dismissButton.setOnClickListener {
      dismiss()
    }
    openButton = view.findViewById(R.id.btn_update_open)
    openButton.setOnClickListener {
      val uriUrl = Uri.parse("https://github.com/MarcDonald/Hibi/releases/latest")
      val launchBrowser = Intent(Intent.ACTION_VIEW)
      launchBrowser.data = uriUrl
      startActivity(launchBrowser)

    }
  }

  private fun setupObservers() {
    viewModel.displayDismiss.observe(this, Observer { value ->
      value?.let { display ->
        dismissButton.show(display)
      }
    })

    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { display ->
        loadingProgressBar.show(display)
        if(display)
          title.text = resources.getString(R.string.checking_for_updates)
      }
    })

    viewModel.displayOpenButton.observe(this, Observer { value ->
      value?.let { display ->
        openButton.show(display)
      }
    })

    viewModel.displayNoUpdateAvailable.observe(this, Observer { value ->
      value?.let { display ->
        noUpdateAvailable.show(display)
        if(display) {
          title.text = resources.getString(R.string.no_update_title)
          message.text = resources.getString(R.string.no_update_message)
          message.show(true)
        }
      }
    })

    viewModel.displayNoConnection.observe(this, Observer { value ->
      value?.let { display ->
        noConnection.show(display)
        if(display) {
          title.text = resources.getString(R.string.no_connection_warning)
        }
      }
    })

    viewModel.newVersionName.observe(this, Observer { value ->
      value?.let { versionName ->
        title.text = resources.getString(R.string.new_version_available_title)
        message.text = resources.getString(R.string.new_version_available_message, versionName)
        message.show(true)
        updateAvailable.show(true)
      }
    })

    viewModel.displayError.observe(this, Observer { value ->
      value?.let { display ->
        errorDisplay.show(display)
        if(display)
          title.text = resources.getString(R.string.generic_error)
      }
    })
  }
}
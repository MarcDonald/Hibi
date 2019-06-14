package app.marcdev.hibi.maintabs.settings.restoredialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.RESTORE_FILE_PATH_KEY
import app.marcdev.hibi.internal.base.HibiDialogFragment
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RestoreDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: RestoreDialogViewModelFactory by instance()
  private lateinit var viewModel: RestoreDialogViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var loadingProgressBar: ProgressBar
  private lateinit var cancelButton: MaterialButton
  private lateinit var messageDisplay: TextView
  private lateinit var confirmButton: MaterialButton
  private lateinit var dismissButton: MaterialButton
  private lateinit var title: TextView
  private lateinit var errorDisplay: ImageView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestoreDialogViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_restore, container, false)

    arguments?.let {
      viewModel.passArguments(requireArguments().getString(RESTORE_FILE_PATH_KEY, ""))
    }

    bindViews(view)
    setupObservers()
    return view
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_restore_title)
    messageDisplay = view.findViewById(R.id.txt_restore_message)
    loadingProgressBar = view.findViewById(R.id.prog_restore)
    errorDisplay = view.findViewById(R.id.img_restore_fail)
    cancelButton = view.findViewById(R.id.btn_restore_cancel)
    cancelButton.setOnClickListener {
      dismiss()
    }
    confirmButton = view.findViewById(R.id.btn_restore_confirm)
    confirmButton.setOnClickListener {
      viewModel.restore()
    }
    dismissButton = view.findViewById(R.id.btn_restore_dismiss)
    dismissButton.setOnClickListener {
      dismiss()
    }
  }

  private fun setupObservers() {
    viewModel.displayButtons.observe(this, Observer { value ->
      value?.let { display ->
        cancelButton.visibility = if(display) View.VISIBLE else View.GONE
        confirmButton.visibility = if(display) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayLoading.observe(this, Observer { value ->
      value?.let { display ->
        if(display) {
          loadingProgressBar.visibility = View.VISIBLE
          title.text = resources.getString(R.string.restoring)
        } else {
          loadingProgressBar.visibility = View.GONE
        }
      }
    })

    viewModel.displayMessage.observe(this, Observer { value ->
      value?.let { display ->
        messageDisplay.visibility = if(display) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayError.observe(this, Observer { value ->
      value?.let { display ->
        if(display) {
          errorDisplay.visibility = View.VISIBLE
          title.text = resources.getString(R.string.restore_error_title)
          messageDisplay.text = resources.getString(R.string.restore_error_message)
          messageDisplay.visibility = View.VISIBLE
        } else {
          errorDisplay.visibility = View.GONE
        }
      }
    })

    viewModel.displayDismiss.observe(this, Observer { value ->
      value?.let { display ->
        dismissButton.visibility = if(display) View.VISIBLE else View.GONE
      }
    })

    viewModel.canDismiss.observe(this, Observer { value ->
      value?.let { canDismiss ->
        isCancelable = canDismiss
      }
    })
  }
}
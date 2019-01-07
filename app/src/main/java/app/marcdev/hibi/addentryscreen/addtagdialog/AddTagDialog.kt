package app.marcdev.hibi.addentryscreen.addtagdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.TransparentSquareButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddTagDialog : ScopedBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var title: TextView
  private lateinit var tagHolder: LinearLayout

  // Viewmodel
  private val viewModelFactory: TagsViewModelFactory by instance()
  private lateinit var viewModel: TagsViewModel

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(TagsViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_add_tags, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_tags_title)
    tagHolder = view.findViewById(R.id.lin_tag_holder)

    val addButton: TransparentSquareButton = view.findViewById(R.id.btn_add_tag)
    addButton.setOnClickListener(addClickListener)

    val saveButton: TransparentSquareButton = view.findViewById(R.id.btn_save_tags)
    saveButton.setOnClickListener(saveClickListener)

    val cancelButton: TransparentSquareButton = view.findViewById(R.id.btn_cancel_tags)
    cancelButton.setOnClickListener(cancelClickListener)

    // TODO remove
    for(x in 0..20) {
      val checkBox = CheckBox(tagHolder.context)
      checkBox.text = "Test $x"
      tagHolder.addView(checkBox)
    }
  }

  private val addClickListener = View.OnClickListener {
    // TODO
    val checkBox = CheckBox(tagHolder.context)
    checkBox.text = "Test"
    tagHolder.addView(checkBox)
  }

  private val saveClickListener = View.OnClickListener {
    // TODO
    Toast.makeText(requireContext(), resources.getString(R.string.save), Toast.LENGTH_SHORT).show()
    dismiss()
  }

  private val cancelClickListener = View.OnClickListener {
    dismiss()
  }
}
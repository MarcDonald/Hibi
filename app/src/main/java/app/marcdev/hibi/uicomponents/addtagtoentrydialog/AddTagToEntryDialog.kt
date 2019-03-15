package app.marcdev.hibi.uicomponents.addtagtoentrydialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.entryscreens.addentryscreen.TagsToSaveToNewEntry
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addtagdialog.AddTagDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddTagToEntryDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var title: TextView
  private lateinit var tagHolder: LinearLayout
  private lateinit var noTagsWarning: TextView

  // Viewmodel
  private val viewModelFactory: AddTagToEntryViewModelFactory by instance()
  private lateinit var viewModel: AddTagToEntryViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_entry_tags, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTagToEntryViewModel::class.java)

    arguments?.let {
      val entryId = arguments!!.getInt(ENTRY_ID_KEY)
      viewModel.entryId = entryId
    }

    displayTags()
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_tags_title)
    tagHolder = view.findViewById(R.id.lin_tags_entry_tag_holder)
    noTagsWarning = view.findViewById(R.id.txt_no_tags_warning)

    val addButton: MaterialButton = view.findViewById(R.id.btn_add_tag)
    addButton.setOnClickListener(addClickListener)

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_tags)
    saveButton.setOnClickListener(saveClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    for(x in 0 until tagHolder.childCount) {
      val tag = tagHolder.getChildAt(x) as CheckBox
      if(tag.isChecked) {
        save(tag.text.toString())
      } else {
        delete(tag.text.toString())
      }
    }
    dismiss()
  }

  private fun save(tag: String) = launch {
    viewModel.saveTagEntryRelation(tag)
  }

  private fun delete(tag: String) = launch {
    viewModel.deleteTagEntryRelation(tag)
  }

  private val addClickListener = View.OnClickListener {
    val dialog = AddTagDialog()
    dialog.show(requireFragmentManager(), "Tag Manager Dialog")
  }

  private fun displayTags() = launch {
    val tagEntryRelations = if(viewModel.entryId == 0) {
      TagsToSaveToNewEntry.list
    } else {
      viewModel.getTagsForEntry()
    }

    val allTags = viewModel.allTags.await()
    allTags.observe(this@AddTagToEntryDialog, Observer { tags ->
      tags.forEach { it ->
        // Gets list of all tags displayed
        val alreadyDisplayedTags = ArrayList<CheckBox>()
        for(x in 0 until tagHolder.childCount) {
          val tag = tagHolder.getChildAt(x) as CheckBox
          alreadyDisplayedTags.add(tag)
        }

        val displayTag = CheckBox(tagHolder.context)
        displayTag.text = it.name

        // If the new tag is already displayed, don't add it
        // This stops it removing user progress before saving
        var addIt = true
        alreadyDisplayedTags.forEach {
          if(it.text == displayTag.text) {
            addIt = false
          }
        }

        if(addIt) {
          if(tagEntryRelations.contains(it.name)) {
            displayTag.isChecked = true
          }
          tagHolder.addView(displayTag)
        }
      }

      if(tagHolder.childCount == 0) {
        noTagsWarning.visibility = View.VISIBLE
      } else {
        noTagsWarning.visibility = View.GONE
      }
    })
  }
}
package app.marcdev.hibi.uicomponents.addtagtoentrydialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addtagdialog.AddTagDialog
import app.marcdev.hibi.uicomponents.views.CheckBoxWithId
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddTagToEntryDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddTagToEntryViewModelFactory by instance()
  private lateinit var viewModel: AddTagToEntryViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var title: TextView
  private lateinit var tagHolder: LinearLayout
  private lateinit var noTagsWarning: TextView
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTagToEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_entry_tags, container, false)
    bindViews(view)
    setupObservers()
    viewModel.loadData()
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    arguments?.let {
      viewModel.passArguments(arguments!!.getInt(ENTRY_ID_KEY, 0))
    }
  }

  private fun bindViews(view: View) {
    title = view.findViewById(R.id.txt_add_tags_title)
    tagHolder = view.findViewById(R.id.lin_tags_entry_tag_holder)
    noTagsWarning = view.findViewById(R.id.txt_no_tags_warning)

    val addButton: MaterialButton = view.findViewById(R.id.btn_add_tag)
    addButton.setOnClickListener {
      val dialog = AddTagDialog()
      dialog.show(requireFragmentManager(), "Tag Manager Dialog")
    }

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save_tag)
    saveButton.setOnClickListener(saveClickListener)
  }

  private fun setupObservers() {
    viewModel.allTags.observe(this, Observer { value ->
      value?.let { tags ->
        tags.forEach { tag ->
          // Gets list of all tags currently displayed
          val alreadyDisplayedTags = ArrayList<CheckBoxWithId>()
          for(x in 0 until tagHolder.childCount) {
            val tagCheckBox = tagHolder.getChildAt(x) as CheckBoxWithId
            alreadyDisplayedTags.add(tagCheckBox)
          }

          val displayTag = CheckBoxWithId(tagHolder.context)
          displayTag.text = tag.name
          displayTag.itemId = tag.id
          if(theme == R.style.Hibi_DarkTheme_BottomSheetDialogTheme) {
            displayTag.setTextColor(resources.getColor(R.color.darkThemePrimaryText, null))
          } else {
            displayTag.setTextColor(resources.getColor(R.color.lightThemePrimaryText, null))
          }

          // If the new tag is already displayed, don't add it
          // This stops it removing user progress before saving
          var addIt = true
          alreadyDisplayedTags.forEach { alreadyDisplayedTag ->
            if(alreadyDisplayedTag.itemId == displayTag.itemId) {
              addIt = false
            }
          }

          if(addIt)
            tagHolder.addView(displayTag)
        }
      }
    })

    viewModel.displayNoTagsWarning.observe(this, Observer { value ->
      value?.let { show ->
        noTagsWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.tagEntryRelations.observe(this, Observer { value ->
      value?.let { tagEntryRelations ->
        tagEntryRelations.forEach { tagId ->
          for(x in 0 until tagHolder.childCount) {
            val tagCheckBox = tagHolder.getChildAt(x) as CheckBoxWithId
            if(tagCheckBox.itemId == tagId)
              tagCheckBox.isChecked = true
          }
        }
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })
  }

  private val saveClickListener = View.OnClickListener {
    val list = arrayListOf<Pair<Int, Boolean>>()
    for(x in 0 until tagHolder.childCount) {
      val checkBox = tagHolder.getChildAt(x) as CheckBoxWithId
      list.add(Pair(checkBox.itemId, checkBox.isChecked))
    }
    viewModel.onSaveClick(list)
  }
}
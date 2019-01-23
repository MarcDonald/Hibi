package app.marcdev.hibi.newwordsdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.addentryscreen.NewWordsToSaveToNewEntry
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.IS_EDIT_MODE_KEY
import app.marcdev.hibi.internal.base.ScopedBottomSheetDialogFragment
import app.marcdev.hibi.newwordsdialog.addnewworddialog.AddNewWordDialog
import app.marcdev.hibi.uicomponents.TransparentSquareButton
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class NewWordDialog : ScopedBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // UI Components
  private lateinit var noResultsWarning: LinearLayout
  private lateinit var addButton: TransparentSquareButton

  // Viewmodel
  private val viewModelFactory: NewWordViewModelFactory by instance()
  private lateinit var viewModel: NewWordViewModel

  // Recycler
  private lateinit var recyclerAdapter: NewWordsRecyclerAdapter
  private lateinit var recycler: RecyclerView

  // Other
  private var isEditMode = true

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_new_word, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewWordViewModel::class.java)

    arguments?.let {
      val entryId = arguments!!.getInt(ENTRY_ID_KEY)
      viewModel.entryId = entryId

      isEditMode = arguments!!.getBoolean(IS_EDIT_MODE_KEY, true)
    }

    if(!isEditMode)
      addButton.visibility = View.GONE

    displayData()
  }

  private fun bindViews(view: View) {
    noResultsWarning = view.findViewById(R.id.lin_new_words_no_results)
    recycler = view.findViewById(R.id.recycler_new_words)

    addButton = view.findViewById(R.id.btn_add_new_word)
    addButton.setOnClickListener(addClickListener)

    initRecycler()
  }

  private val addClickListener = View.OnClickListener {
    val dialog = AddNewWordDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add New Word Dialog")
  }

  private fun initRecycler() {
    this.recyclerAdapter = NewWordsRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
    recycler.addItemDecoration(dividerItemDecoration)
  }

  private fun displayData() = launch {
    if(viewModel.entryId != 0) {
      val newWords = viewModel.newWords.await()
      newWords.observe(this@NewWordDialog, Observer {
        recyclerAdapter.updateList(it)

        if(it.isNotEmpty()) {
          noResultsWarning.visibility = View.GONE
        } else {
          noResultsWarning.visibility = View.VISIBLE
        }
      })
    } else {
      val list = NewWordsToSaveToNewEntry.list
      recyclerAdapter.updateList(list)

      if(list.isNotEmpty()) {
        noResultsWarning.visibility = View.GONE
      } else {
        noResultsWarning.visibility = View.VISIBLE
      }
    }
  }
}
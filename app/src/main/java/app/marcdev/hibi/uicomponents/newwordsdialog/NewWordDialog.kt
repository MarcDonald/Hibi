package app.marcdev.hibi.uicomponents.newwordsdialog

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
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.IS_EDIT_MODE_KEY
import app.marcdev.hibi.internal.base.HibiBottomSheetDialogFragment
import app.marcdev.hibi.uicomponents.addnewworddialog.AddNewWordDialog
import com.google.android.material.button.MaterialButton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class NewWordDialog : HibiBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: NewWordViewModelFactory by instance()
  private lateinit var viewModel: NewWordViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var noResultsWarning: LinearLayout
  private lateinit var addButton: MaterialButton
  private lateinit var recyclerAdapter: NewWordsRecyclerAdapter
  private lateinit var recycler: RecyclerView
  // </editor-fold>

  // <editor-fold desc="Other">
  private var isEditMode = true
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewWordViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_new_word, container, false)
    arguments?.let { arguments ->
      viewModel.passArguments(arguments.getInt(ENTRY_ID_KEY, 0), arguments.getBoolean(IS_EDIT_MODE_KEY, true))
    }
    bindViews(view)
    setupObservers()
    return view
  }

  private fun bindViews(view: View) {
    noResultsWarning = view.findViewById(R.id.lin_new_words_no_results)
    recycler = view.findViewById(R.id.recycler_new_words)

    addButton = view.findViewById(R.id.btn_add_new_word)
    addButton.setOnClickListener(addClickListener)

    initRecycler()
  }

  private fun setupObservers() {
    viewModel.displayAddButton.observe(this, Observer { value ->
      value?.let { show ->
        addButton.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.allowEdits.observe(this, Observer { value ->
      value?.let { allow ->
        recyclerAdapter.isEditMode = allow
      }
    })

    viewModel.displayNoWords.observe(this, Observer { value ->
      value?.let { show ->
        noResultsWarning.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.getNewWords().observe(this, Observer { value ->
      value?.let { list ->
        viewModel.listReceived(list.isEmpty())
        recyclerAdapter.updateList(list)
      }
    })
  }

  private val addClickListener = View.OnClickListener {
    val dialog = AddNewWordDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add New Word Dialog")
  }

  private fun initRecycler() {
    this.recyclerAdapter = NewWordsRecyclerAdapter(requireContext(), requireFragmentManager())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
    recycler.addItemDecoration(dividerItemDecoration)
  }
}
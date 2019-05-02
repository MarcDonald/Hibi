package app.marcdev.hibi.maintabs.tagsfragment.taggedentriesfragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.PREF_ENTRY_DIVIDERS
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.mainentriesrecycler.EntriesRecyclerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class TaggedEntriesFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: TaggedEntriesViewModelFactory by instance()
  private lateinit var viewModel: TaggedEntriesViewModel

  // UI
  private lateinit var loadingDisplay: ConstraintLayout
  private lateinit var noResults: ConstraintLayout
  private lateinit var toolbarTitle: TextView

  // RecyclerView
  private lateinit var recyclerAdapter: EntriesRecyclerAdapter

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaggedEntriesViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_tagged_entries, container, false)

    bindViews(view)
    initRecycler(view)

    launch {
      arguments?.let {
        val tagID: Int = TaggedEntriesFragmentArgs.fromBundle(it).tagID
        viewModel.updateList(tagID)
        toolbarTitle.text = viewModel.getTagName(tagID)
      }
    }

    return view
  }

  private fun bindViews(view: View) {
    loadingDisplay = view.findViewById(R.id.const_tagged_entries_loading)
    noResults = view.findViewById(R.id.const_no_tagged_entries)
    val toolbarBack: ImageView = view.findViewById(R.id.img_back_toolbar_back)
    toolbarBack.setOnClickListener {
      Navigation.findNavController(requireView()).popBackStack()
    }
    toolbarTitle = view.findViewById(R.id.txt_back_toolbar_title)
  }

  private fun initRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.recycler_tagged_entries)
    this.recyclerAdapter = EntriesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    recycler.adapter = recyclerAdapter
    recycler.layoutManager = layoutManager

    val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
    if(includeEntryDividers) {
      val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
      recycler.addItemDecoration(dividerItemDecoration)
    }

    displayRecyclerData()
  }

  private fun displayRecyclerData() = launch {
    loadingDisplay.visibility = View.VISIBLE
    noResults.visibility = View.GONE

    val displayItems = viewModel.displayItems
    displayItems.observe(this@TaggedEntriesFragment, Observer { items ->
      recyclerAdapter.updateList(items)

      if(items.isEmpty())
        noResults.visibility = View.VISIBLE
      else
        noResults.visibility = View.GONE
      loadingDisplay.visibility = View.GONE
    })
  }
}
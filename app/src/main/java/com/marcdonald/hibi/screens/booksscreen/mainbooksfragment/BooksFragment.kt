package com.marcdonald.hibi.screens.booksscreen.mainbooksfragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_ENTRY_DIVIDERS
import com.marcdonald.hibi.internal.extension.show
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BooksFragment : Fragment(), KodeinAware {
	override val kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: BooksFragmentViewModelFactory by instance()
	private lateinit var viewModel: BooksFragmentViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var loadingDisplay: ConstraintLayout
	private lateinit var noResults: ConstraintLayout
	private lateinit var recyclerAdapter: BooksRecyclerAdapter
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(BooksFragmentViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_books, container, false)
		bindViews(view)
		initRecycler(view)
		setupObservers()
		return view
	}

	private fun bindViews(view: View) {
		loadingDisplay = view.findViewById(R.id.const_books_loading)
		noResults = view.findViewById(R.id.const_no_books)
	}

	private fun initRecycler(view: View) {
		val recycler: RecyclerView = view.findViewById(R.id.recycler_books)
		this.recyclerAdapter = BooksRecyclerAdapter(requireContext(), requireFragmentManager())
		val layoutManager = LinearLayoutManager(context)
		recycler.adapter = recyclerAdapter
		recycler.layoutManager = layoutManager

		val includeEntryDividers = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_ENTRY_DIVIDERS, true)
		if(includeEntryDividers) {
			val dividerItemDecoration = DividerItemDecoration(recycler.context, layoutManager.orientation)
			recycler.addItemDecoration(dividerItemDecoration)
		}
	}

	private fun setupObservers() {
		viewModel.books.observe(this, Observer { value ->
			value?.let { list ->
				viewModel.listReceived(list.isEmpty())
				recyclerAdapter.updateList(list)
			}
		})

		viewModel.displayLoading.observe(this, Observer { value ->
			value?.let { shouldShow ->
				loadingDisplay.show(shouldShow)
			}
		})

		viewModel.displayNoResults.observe(this, Observer { value ->
			value?.let { shouldShow ->
				noResults.show(shouldShow)
			}
		})
	}
}
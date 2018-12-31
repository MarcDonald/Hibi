package app.marcdev.nichiroku.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.nichiroku.R
import app.marcdev.nichiroku.internal.base.ScopedFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber

class MainScreenFragment : ScopedFragment(), KodeinAware {

  override val kodein by closestKodein()

  lateinit var loadingDisplay: ConstraintLayout
  lateinit var entriesRecycler: RecyclerView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.d("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_main_screen, container, false)

    bindViews(view)

    return view
  }

  private fun bindViews(view: View) {
    loadingDisplay = view.findViewById(R.id.const_entries_loading)
    entriesRecycler = view.findViewById(R.id.recycler_entries)
    val fab: FloatingActionButton = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabClickListener)
  }

  private val fabClickListener = View.OnClickListener {
    Timber.d("Log: Main Fab Clicked")
  }
}
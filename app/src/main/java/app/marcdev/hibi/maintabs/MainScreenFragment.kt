package app.marcdev.hibi.maintabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.booksfragment.BooksFragment
import app.marcdev.hibi.maintabs.calendarfragment.CalendarFragment
import app.marcdev.hibi.maintabs.mainentries.MainEntriesFragment
import app.marcdev.hibi.maintabs.tagsfragment.TagsFragment
import com.google.android.material.tabs.TabLayout
import timber.log.Timber


class MainScreenFragment : ScopedFragment() {

  private lateinit var pageAdapter: MainScreenPageAdapter
  private lateinit var viewPager: ViewPager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    pageAdapter = MainScreenPageAdapter(requireFragmentManager())

    viewPager = view.findViewById(R.id.view_pager_main)
    setupViewPager(viewPager)

    val tabLayout: TabLayout = view.findViewById(R.id.tabs_main)
    tabLayout.setupWithViewPager(viewPager)
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = MainScreenPageAdapter(requireFragmentManager())
    adapter.addFragment(MainEntriesFragment(), resources.getString(R.string.tab_entries))
    adapter.addFragment(CalendarFragment(), resources.getString(R.string.tab_calendar))
    adapter.addFragment(TagsFragment(), resources.getString(R.string.tab_tags))
    adapter.addFragment(BooksFragment(), resources.getString(R.string.tab_books))
    viewPager.adapter = adapter
  }
}
package app.marcdev.hibi.maintabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.booksfragment.BooksFragment
import app.marcdev.hibi.maintabs.calendarfragment.CalendarFragment
import app.marcdev.hibi.maintabs.mainentries.MainEntriesFragment
import app.marcdev.hibi.maintabs.tagsfragment.TagsFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import timber.log.Timber


class MainScreenFragment : ScopedFragment() {

  private lateinit var pageAdapter: MainScreenPageAdapter
  private lateinit var viewPager: ViewPager

  private lateinit var fab: MaterialButton

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    return inflater.inflate(app.marcdev.hibi.R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    fab = view.findViewById(app.marcdev.hibi.R.id.fab_main)
    fab.setOnClickListener(clickListener)

    pageAdapter = MainScreenPageAdapter(requireFragmentManager())

    viewPager = view.findViewById(app.marcdev.hibi.R.id.view_pager_main)
    viewPager.addOnPageChangeListener(pageChangeListener)
    setupViewPager(viewPager)

    val tabLayout: TabLayout = view.findViewById(app.marcdev.hibi.R.id.tabs_main)
    tabLayout.setupWithViewPager(viewPager)
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = MainScreenPageAdapter(requireFragmentManager())
    adapter.addFragment(MainEntriesFragment(), resources.getString(app.marcdev.hibi.R.string.tab_entries))
    adapter.addFragment(CalendarFragment(), resources.getString(app.marcdev.hibi.R.string.tab_calendar))
    adapter.addFragment(TagsFragment(), resources.getString(app.marcdev.hibi.R.string.tab_tags))
    adapter.addFragment(BooksFragment(), resources.getString(app.marcdev.hibi.R.string.tab_books))
    viewPager.adapter = adapter
  }

  private val pageChangeListener = object : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {/*Necessary*/
    }

    override fun onPageSelected(position: Int) {
      when (position) {
        0 -> fab.text = resources.getString(R.string.create_entry)
        1 -> fab.text = resources.getString(R.string.create_entry)
        2 -> fab.text = resources.getString(R.string.create_tag)
        3 -> fab.text = resources.getString(R.string.create_book)
      }
    }

    override fun onPageScrollStateChanged(state: Int) {/*Necessary*/
    }
  }

  private val clickListener = View.OnClickListener {
    //    val addEntryAction = MainScreenFragmentDirections.addEntryAction()
//    Navigation.findNavController(it).navigate(addEntryAction)
    when (viewPager.currentItem) {
      0 -> Toast.makeText(requireContext(), resources.getString(R.string.create_entry), Toast.LENGTH_SHORT).show()
      1 -> Toast.makeText(requireContext(), resources.getString(R.string.create_entry), Toast.LENGTH_SHORT).show()
      2 -> Toast.makeText(requireContext(), resources.getString(R.string.create_tag), Toast.LENGTH_SHORT).show()
      3 -> Toast.makeText(requireContext(), resources.getString(R.string.create_book), Toast.LENGTH_SHORT).show()
    }
  }
}
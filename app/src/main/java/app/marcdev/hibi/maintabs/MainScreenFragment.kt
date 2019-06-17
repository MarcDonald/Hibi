package app.marcdev.hibi.maintabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.BOOKS_TAB
import app.marcdev.hibi.internal.CALENDAR_TAB
import app.marcdev.hibi.internal.ENTRIES_TAB
import app.marcdev.hibi.internal.TAGS_TAB
import app.marcdev.hibi.maintabs.booksfragment.mainbooksfragment.BooksFragment
import app.marcdev.hibi.maintabs.calendarfragment.CalendarFragment
import app.marcdev.hibi.maintabs.mainentries.MainEntriesFragment
import app.marcdev.hibi.maintabs.tagsfragment.maintagsfragment.TagsFragment
import app.marcdev.hibi.uicomponents.addbookdialog.AddBookDialog
import app.marcdev.hibi.uicomponents.addtagdialog.AddTagDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import timber.log.Timber

class MainScreenFragment : Fragment() {

  // <editor-fold desc="UI Components">
  private lateinit var viewPager: ViewPager
  private lateinit var fab: MaterialButton
  private lateinit var mainMenu: MainScreenMenuDialog
  // </editor-fold>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bindViews(view)

    viewPager = view.findViewById(R.id.view_pager_main)
    viewPager.addOnPageChangeListener(pageChangeListener)
    setupViewPager(viewPager)

    val tabLayout: TabLayout = view.findViewById(R.id.tabs_main)
    tabLayout.setupWithViewPager(viewPager)

    mainMenu = MainScreenMenuDialog()
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = MainScreenPageAdapter(childFragmentManager)
    adapter.addFragment(MainEntriesFragment(), resources.getString(R.string.tab_entries))
    adapter.addFragment(CalendarFragment(), resources.getString(R.string.tab_calendar))
    adapter.addFragment(TagsFragment(), resources.getString(R.string.tab_tags))
    adapter.addFragment(BooksFragment(), resources.getString(R.string.tab_books))
    viewPager.adapter = adapter
  }

  private fun bindViews(view: View) {
    fab = view.findViewById(R.id.fab_main)
    fab.setOnClickListener(fabClickListener)

    val bottomLeftButton: ImageView = view.findViewById(R.id.img_main_bot_left)
    bottomLeftButton.setOnClickListener(bottomLeftClickListener)
    val bottomRightButton: ImageView = view.findViewById(R.id.img_main_bot_right)
    bottomRightButton.setOnClickListener(bottomRightClickListener)
  }

  private val pageChangeListener = object : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {/*Necessary*/
    }

    override fun onPageSelected(position: Int) {
      when(position) {
        ENTRIES_TAB, CALENDAR_TAB -> fab.text = resources.getString(R.string.fab_create_entry)
        TAGS_TAB -> fab.text = resources.getString(R.string.fab_create_tag)
        BOOKS_TAB -> fab.text = resources.getString(R.string.fab_create_book)
      }
    }

    override fun onPageScrollStateChanged(state: Int) {/*Necessary*/
    }
  }

  private val fabClickListener = View.OnClickListener {
    when(viewPager.currentItem) {
      ENTRIES_TAB, CALENDAR_TAB -> {
        val addEntryAction = MainScreenFragmentDirections.addEntryAction()
        Navigation.findNavController(requireView()).navigate(addEntryAction)
      }
      TAGS_TAB -> {
        val dialog = AddTagDialog()
        dialog.show(requireFragmentManager(), "Add Tag Dialog")
      }
      BOOKS_TAB -> {
        val dialog = AddBookDialog()
        dialog.show(requireFragmentManager(), "Add Book Dialog")
      }
    }
  }

  private val bottomLeftClickListener = View.OnClickListener {
    mainMenu.show(requireFragmentManager(), "Main Menu Dialog")
  }

  private val bottomRightClickListener = View.OnClickListener {
    val searchEntriesAction = MainScreenFragmentDirections.searchEntriesAction()
    Navigation.findNavController(requireView()).navigate(searchEntriesAction)
  }
}
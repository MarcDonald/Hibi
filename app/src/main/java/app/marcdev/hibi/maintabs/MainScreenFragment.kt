package app.marcdev.hibi.maintabs

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import app.marcdev.hibi.MainActivity
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.*
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.maintabs.booksfragment.BooksFragment
import app.marcdev.hibi.maintabs.calendarfragment.CalendarFragment
import app.marcdev.hibi.maintabs.mainentries.MainEntriesFragment
import app.marcdev.hibi.maintabs.tagsfragment.TagsFragment
import app.marcdev.hibi.uicomponents.addtagdialog.AddTagDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import timber.log.Timber


class MainScreenFragment : ScopedFragment() {

  private lateinit var viewPager: ViewPager

  private lateinit var fab: MaterialButton
  private lateinit var mainMenu: MainScreenMenuDialog

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    return inflater.inflate(app.marcdev.hibi.R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    bindViews(view)

    viewPager = view.findViewById(app.marcdev.hibi.R.id.view_pager_main)
    viewPager.addOnPageChangeListener(pageChangeListener)
    setupViewPager(viewPager)

    val tabLayout: TabLayout = view.findViewById(app.marcdev.hibi.R.id.tabs_main)
    tabLayout.setupWithViewPager(viewPager)

    mainMenu = MainScreenMenuDialog()
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = MainScreenPageAdapter(childFragmentManager)
    adapter.addFragment(MainEntriesFragment(), resources.getString(app.marcdev.hibi.R.string.tab_entries))
    adapter.addFragment(CalendarFragment(), resources.getString(app.marcdev.hibi.R.string.tab_calendar))
    adapter.addFragment(TagsFragment(), resources.getString(app.marcdev.hibi.R.string.tab_tags))
    adapter.addFragment(BooksFragment(), resources.getString(app.marcdev.hibi.R.string.tab_books))
    viewPager.adapter = adapter
  }

  private fun bindViews(view: View) {
    fab = view.findViewById(app.marcdev.hibi.R.id.fab_main)
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
        Navigation.findNavController(it).navigate(addEntryAction)
      }
      TAGS_TAB -> {
        val dialog = AddTagDialog()
        dialog.show(requireFragmentManager(), "Add Tag Dialog")
      }
      BOOKS_TAB -> Toast.makeText(requireContext(), resources.getString(R.string.fab_create_book), Toast.LENGTH_SHORT).show()
    }
  }

  private val bottomLeftClickListener = View.OnClickListener {
    mainMenu.show(requireFragmentManager(), "Main Menu Dialog")
  }

  private val bottomRightClickListener = View.OnClickListener {
    Toast.makeText(requireContext(), "Search", Toast.LENGTH_SHORT).show()
    sendNotification()
  }

  private fun sendNotification() {
    val intent = Intent().apply {
      action = ADD_ENTRY_NOTIFICATION_INTENT_ACTION
      setPackage(PACKAGE)
      setClass(requireContext(), MainActivity::class.java)
    }
    val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)

    val notification = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_REMINDER_ID)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setCategory(NotificationCompat.CATEGORY_REMINDER)
      .setAutoCancel(true)
      .setSmallIcon(R.drawable.ic_reminder_notification)
      .setContentTitle(resources.getString(R.string.reminder_notification_title))
      .setContentText(resources.getString(R.string.reminder_notification_description))
      .addAction(R.drawable.ic_add_black_24dp, resources.getString(R.string.reminder_notification_add_action), pendingIntent)
      .setContentIntent(pendingIntent)
      .build()

    val manager = NotificationManagerCompat.from(requireContext())
    manager.notify(REMINDER_NOTIFICATION_ID, notification)
  }
}
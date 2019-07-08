/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.screens.mainscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.BOOKS_TAB
import com.marcdonald.hibi.internal.CALENDAR_TAB
import com.marcdonald.hibi.internal.ENTRIES_TAB
import com.marcdonald.hibi.internal.TAGS_TAB
import com.marcdonald.hibi.screens.booksscreen.mainbooksfragment.BooksFragment
import com.marcdonald.hibi.screens.calendarscreen.CalendarFragment
import com.marcdonald.hibi.screens.mainentries.MainEntriesFragment
import com.marcdonald.hibi.screens.tagsscreen.maintagsfragment.TagsFragment
import com.marcdonald.hibi.uicomponents.addbookdialog.AddBookDialog
import com.marcdonald.hibi.uicomponents.addtagdialog.AddTagDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MainScreenFragment : Fragment(), KodeinAware {
	override val kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: MainScreenViewModelFactory by instance()
	private lateinit var viewModel: MainScreenViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var viewPager: ViewPager
	private lateinit var fab: MaterialButton
	private lateinit var mainMenu: MainScreenMenuDialog
	private lateinit var updateSnackbar: Snackbar
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainScreenViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_main, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		bindViews(view)
		setupObservers()
		setupViewPager(view)
		viewModel.checkForUpdatesIfShould()
		val tabLayout: TabLayout = view.findViewById(R.id.tabs_main)
		tabLayout.setupWithViewPager(viewPager)
	}

	private fun setupObservers() {
		viewModel.newVersion.observe(this, Observer { value ->
			value?.let { newVersionName ->
				if(newVersionName.isNotBlank() && !updateSnackbar.isShownOrQueued) {
					updateSnackbar.setText(resources.getString(R.string.new_version_available_message, newVersionName))
					updateSnackbar.show()
				}
			}
		})
	}

	private fun setupViewPager(view: View) {
		viewPager = view.findViewById(R.id.view_pager_main)
		viewPager.addOnPageChangeListener(pageChangeListener)
		val adapter = MainScreenPageAdapter(childFragmentManager)
		adapter.addFragment(MainEntriesFragment(), resources.getString(R.string.tab_entries))
		adapter.addFragment(CalendarFragment(), resources.getString(R.string.tab_calendar))
		adapter.addFragment(TagsFragment(), resources.getString(R.string.tab_tags))
		adapter.addFragment(BooksFragment(), resources.getString(R.string.tab_books))
		viewPager.adapter = adapter
	}

	private fun bindViews(view: View) {
		mainMenu = MainScreenMenuDialog()

		fab = view.findViewById(R.id.fab_main)
		fab.setOnClickListener(fabClickListener)

		val bottomLeftButton: ImageView = view.findViewById(R.id.img_main_bot_left)
		bottomLeftButton.setOnClickListener(bottomLeftClickListener)
		val bottomRightButton: ImageView = view.findViewById(R.id.img_main_bot_right)
		bottomRightButton.setOnClickListener(bottomRightClickListener)

		updateSnackbar = Snackbar.make(requireView(), resources.getString(R.string.new_version_available_message), Snackbar.LENGTH_SHORT)
		updateSnackbar.setAction(resources.getString(R.string.download)) {
			val uriUrl = Uri.parse("https://www.github.com/MarcDonald/Hibi/releases/latest")
			val launchBrowser = Intent(Intent.ACTION_VIEW)
			launchBrowser.data = uriUrl
			startActivity(launchBrowser)
		}
	}

	private val pageChangeListener = object : ViewPager.OnPageChangeListener {
		override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {/*Necessary*/
		}

		override fun onPageSelected(position: Int) {
			when(position) {
				ENTRIES_TAB, CALENDAR_TAB -> fab.text = resources.getString(R.string.fab_create_entry)
				TAGS_TAB                  -> fab.text = resources.getString(R.string.fab_create_tag)
				BOOKS_TAB                 -> fab.text = resources.getString(R.string.fab_create_book)
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
			TAGS_TAB                  -> {
				val dialog = AddTagDialog()
				dialog.show(requireFragmentManager(), "Add Tag Dialog")
			}
			BOOKS_TAB                 -> {
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
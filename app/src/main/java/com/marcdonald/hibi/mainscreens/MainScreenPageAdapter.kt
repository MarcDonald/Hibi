package com.marcdonald.hibi.mainscreens

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainScreenPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
  private val fragmentList = ArrayList<Fragment>()
  private val fragmentTitleList = ArrayList<String>()

  override fun getItem(position: Int): Fragment {
    return fragmentList[position]
  }

  override fun getCount(): Int {
    return fragmentList.size
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return fragmentTitleList[position]
  }

  fun addFragment(fragment: Fragment, title: String) {
    fragmentList.add(fragment)
    fragmentTitleList.add(title)
  }
}
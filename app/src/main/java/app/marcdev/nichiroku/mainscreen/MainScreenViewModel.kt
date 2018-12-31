package app.marcdev.nichiroku.mainscreen

import androidx.lifecycle.ViewModel
import app.marcdev.nichiroku.data.entity.Entry
import app.marcdev.nichiroku.data.repository.EntryRepository
import app.marcdev.nichiroku.internal.lazyDeferred

class MainScreenViewModel(private val entryRepository: EntryRepository) : ViewModel() {

  val allEntries by lazyDeferred {
    entryRepository.getAllEntries()
  }

  // TODO this is just for testing, remove in the future
  val lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam non pulvinar orci. Nam porta, arcu sed vestibulum feugiat, mauris dolor dapibus odio, sed congue nulla magna tincidunt lacus. Nullam efficitur nibh enim, nec fringilla nulla tempor vel. Donec feugiat ipsum mauris, in maximus mi commodo at. Cras tempus ac tellus et euismod. Integer id mi mollis, ultrices lectus eu, faucibus sapien. Fusce eget massa at nulla convallis vestibulum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ante ante, pulvinar nec est ut, ornare euismod lectus. Nulla facilisi. Nullam non elementum arcu. Ut nec nibh bibendum, interdum enim eget, mollis tortor. Donec id tempor nibh. Donec porttitor arcu ligula, et eleifend metus vestibulum at. Vivamus convallis dapibus leo. Proin et magna eget mi tincidunt aliquam."

  suspend fun addLongEntry() {
    entryRepository.addEntry(Entry("Test Date", "Test Time", lorem))
  }

  suspend fun addShortEntry() {
    entryRepository.addEntry(Entry("Test Date", "Test Time", "Test Content"))
  }
}
package app.marcdev.hibi.maintabs.searchentries.searchentriescriteriadialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.maintabs.searchentries.EntrySearchCriteria
import app.marcdev.hibi.maintabs.searchentries.SearchCriteriaChangeListener

class SearchEntriesCriteriaDialogViewModel : ViewModel() {
  private var _listener: SearchCriteriaChangeListener? = null
  private val _criteria = EntrySearchCriteria()

  private val _beginningDisplay = MutableLiveData<String>()
  val beginningDisplay: LiveData<String>
    get() = _beginningDisplay

  private val _endDisplay = MutableLiveData<String>()
  val endDisplay: LiveData<String>
    get() = _endDisplay

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  fun setCriteriaChangeListener(criteriaChangeListener: SearchCriteriaChangeListener) {
    _listener = criteriaChangeListener
  }

  fun reset() {
    resetStartDate()
    resetEndDate()
  }

  fun setStartDate(year: Int, month: Int, day: Int) {
    _criteria.startYear = year
    _criteria.startMonth = month
    _criteria.startDay = day
    _beginningDisplay.value = formatDateForDisplay(day, month, year)
  }

  fun setEndDate(year: Int, month: Int, day: Int) {
    _criteria.endYear = year
    _criteria.endMonth = month
    _criteria.endDay = day
    _endDisplay.value = formatDateForDisplay(day, month, year)
  }

  fun search() {
    _listener?.onSearchCriteriaChange(_criteria)
    _dismiss.value = true
  }

  fun resetStartDate() {
    _criteria.startYear = 0
    _criteria.startDay = 1
    _criteria.startMonth = 0
    _beginningDisplay.value = ""
  }

  fun resetEndDate() {
    _criteria.endYear = 9999
    _criteria.endDay = 31
    _criteria.endMonth = 11
    _endDisplay.value = ""
  }
}
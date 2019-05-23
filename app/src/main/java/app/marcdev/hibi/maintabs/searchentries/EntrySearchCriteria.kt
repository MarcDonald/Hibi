package app.marcdev.hibi.maintabs.searchentries

import timber.log.Timber

class EntrySearchCriteria {
  var startYear: Int = 0
    set(value) {
      if(value in 0..9999)
        field = value
      else
        Timber.e("Log: setStartYear: $value is out of the 0..9999 bounds")
    }

  var startMonth: Int = 0
    set(value) {
      if(value in 0..11)
        field = value
      else
        Timber.e("Log: setStartMonth: $value is out of the 0..11 bounds")
    }

  var startDay: Int = 1
    set(value) {
      if(value in 1..31)
        field = value
      else
        Timber.e("Log: setStartDay: $value is out of the 1..31 bounds")
    }

  var endYear: Int = 9999
    set(value) {
      if(value in 0..9999)
        field = value
      else
        Timber.e("Log: setEndYear: $value is out of the 0..9999 bounds")
    }

  var endMonth: Int = 11
    set(value) {
      if(value in 0..11)
        field = value
      else
        Timber.e("Log: setEndMonth: $value is out of the 0..11 bounds")
    }

  var endDay: Int = 31
    set(value) {
      if(value in 1..31)
        field = value
      else
        Timber.e("Log: setEndDay: $value is out of the 1..31 bounds")
    }

  var content: String = ""

  var location: String = ""

  var tags: List<Int> = listOf()

  var books: List<Int> = listOf()

  override fun toString(): String {
    return "Start Date: $startYear $startMonth $startDay End Date: $endYear $endMonth $endDay Containing: $content"
  }
}
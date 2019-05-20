package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import java.util.*


class DateTimeStore {
  private val _readableDate = MutableLiveData<String>()
  val readableDate: LiveData<String>
    get() = _readableDate

  private val _readableTime = MutableLiveData<String>()
  val readableTime: LiveData<String>
    get() = _readableTime

  private var calendar = Calendar.getInstance()

  init {
    _readableDate.value = formatDateForDisplay(calendar)
    _readableTime.value = formatTimeForDisplay(calendar)
  }

  fun setDate(day: Int, month: Int, year: Int) {
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    _readableDate.value = formatDateForDisplay(calendar)
  }

  fun setTime(hour: Int, minute: Int) {
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    _readableTime.value = formatTimeForDisplay(calendar)
  }

  fun getDay(): Int {
    return calendar.get(Calendar.DAY_OF_MONTH)
  }

  fun getMonth(): Int {
    return calendar.get(Calendar.MONTH)
  }

  fun getYear(): Int {
    return calendar.get(Calendar.YEAR)
  }

  fun getHour(): Int {
    return calendar.get(Calendar.HOUR_OF_DAY)
  }

  fun getMinute(): Int {
    return calendar.get(Calendar.MINUTE)
  }
}
package app.marcdev.nichiroku.addentryscreen

import androidx.lifecycle.MutableLiveData
import app.marcdev.nichiroku.formatDateForDisplay
import app.marcdev.nichiroku.formatTimeForDisplay
import java.util.*


class DateTimeStore {
  val readableDate: MutableLiveData<String> = MutableLiveData()
  val readableTime: MutableLiveData<String> = MutableLiveData()

  private var calendar = Calendar.getInstance()

  init {
    readableDate.value = formatDateForDisplay(calendar)
    readableTime.value = formatTimeForDisplay(calendar)
  }

  fun setDate(day: Int, month: Int, year: Int) {
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    readableDate.value = formatDateForDisplay(calendar)
  }

  fun setTime(hour: Int, minute: Int) {
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)

    readableTime.value = formatTimeForDisplay(calendar)
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
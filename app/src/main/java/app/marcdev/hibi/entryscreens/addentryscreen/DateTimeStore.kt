package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import java.util.*

class DateTimeStore {
  private var calendar = Calendar.getInstance()

  private val _readableDate = MutableLiveData<String>()
  val readableDate: LiveData<String>
    get() = _readableDate

  private val _readableTime = MutableLiveData<String>()
  val readableTime: LiveData<String>
    get() = _readableTime

  val day: Int
    get() = calendar.get(Calendar.DAY_OF_MONTH)

  val month: Int
    get() = calendar.get(Calendar.MONTH)

  val year: Int
    get() = calendar.get(Calendar.YEAR)

  val hour: Int
    get() = calendar.get(Calendar.HOUR_OF_DAY)

  val minute: Int
    get() = calendar.get(Calendar.MINUTE)

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
}
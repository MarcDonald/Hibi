package app.marcdev.nichiroku

import android.text.format.DateFormat
import java.util.*

fun formatDateForDisplay(calendar: Calendar): String {
  val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEdMMMMyyyy")
  val formattedDate = DateFormat.format(datePattern, calendar)
  return formattedDate.toString()
}

fun formatDateForDisplay(day: Int, month: Int, year: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.DAY_OF_MONTH, day)
  calendar.set(Calendar.MONTH, month)
  calendar.set(Calendar.YEAR, year)
  return formatDateForDisplay(calendar)
}
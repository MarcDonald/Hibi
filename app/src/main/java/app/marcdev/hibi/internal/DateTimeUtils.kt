package app.marcdev.hibi.internal

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

fun formatDateForHeader(month: Int, year: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.DAY_OF_MONTH, 1)
  calendar.set(Calendar.MONTH, month)
  calendar.set(Calendar.YEAR, year)

  val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMyyyy")
  val formattedDate = DateFormat.format(datePattern, calendar)
  return formattedDate.toString()
}

fun formatTimeForDisplay(calendar: Calendar): String {
  val timePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmm")
  val formattedTime = DateFormat.format(timePattern, calendar)
  return formattedTime.toString()
}

fun formatTimeForDisplay(hour: Int, minute: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.HOUR_OF_DAY, hour)
  calendar.set(Calendar.MINUTE, minute)
  return formatTimeForDisplay(calendar)
}

fun formatDateTimeForDisplay(day: Int, month: Int, year: Int, hour: Int, minute: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.DAY_OF_MONTH, day)
  calendar.set(Calendar.MONTH, month)
  calendar.set(Calendar.YEAR, year)
  calendar.set(Calendar.HOUR_OF_DAY, hour)
  calendar.set(Calendar.MINUTE, minute)
  return formatDateTimeForDisplay(calendar)
}

fun formatDateTimeForDisplay(calendar: Calendar): String {
  val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEdMMMMyyyy")
  val formattedDate = DateFormat.format(datePattern, calendar)

  val timePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmm")
  val formattedTime = DateFormat.format(timePattern, calendar)

  return "$formattedDate - $formattedTime"
}
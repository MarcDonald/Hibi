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
package com.marcdonald.hibi.screens.entries.addentry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import java.util.*

class DateTimeStore(private val dateTimeUtils: DateTimeUtils) {
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
		_readableDate.postValue(dateTimeUtils.formatDateForDisplay(calendar))
		_readableTime.postValue(dateTimeUtils.formatTimeForDisplay(calendar))
	}

	fun setDate(day: Int, month: Int, year: Int) {
		calendar.set(Calendar.DAY_OF_MONTH, day)
		calendar.set(Calendar.MONTH, month)
		calendar.set(Calendar.YEAR, year)

		_readableDate.postValue(dateTimeUtils.formatDateForDisplay(calendar))
	}

	fun setTime(hour: Int, minute: Int) {
		calendar.set(Calendar.HOUR_OF_DAY, hour)
		calendar.set(Calendar.MINUTE, minute)

		_readableTime.postValue(dateTimeUtils.formatTimeForDisplay(calendar))
	}
}
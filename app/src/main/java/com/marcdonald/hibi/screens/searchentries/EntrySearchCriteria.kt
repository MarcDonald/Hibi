/*
 * Copyright 2021 Marc Donald
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
package com.marcdonald.hibi.screens.searchentries

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
		return "Start Date: $startYear $startMonth $startDay End Date: $endYear $endMonth $endDay Containing: $content At: $location With Tags: $tags In: $books"
	}
}
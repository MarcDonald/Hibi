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
package com.marcdonald.hibi.screens.main

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.internal.PREF_LAST_UPDATE_CHECK
import com.marcdonald.hibi.internal.PREF_PERIODICALLY_CHECK_FOR_UPDATES
import com.marcdonald.hibi.internal.utils.UpdateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainScreenViewModel(application: Application,
													private val updateUtils: UpdateUtils)
	: AndroidViewModel(application) {

	private val _newVersionName = MutableLiveData<String>()
	val newVersion: LiveData<String>
		get() = _newVersionName

	init {
		_newVersionName.postValue("")
	}

	fun checkForUpdatesIfShould() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
		val now = Calendar.getInstance().timeInMillis
		val lastCheck = prefs.getLong(PREF_LAST_UPDATE_CHECK, 0)

		// Checks if the pref is checked and if it's been a day (86400000ms) since the last check
		// Sets _newVersionName to blank if no update so that the snackbar doesn't get activated again when coming back after popping backstack
		if(prefs.getBoolean(PREF_PERIODICALLY_CHECK_FOR_UPDATES, true) && now > lastCheck + 86400000) {
			prefs.edit().putLong(PREF_LAST_UPDATE_CHECK, now).apply()
			checkForUpdate()
		} else {
			_newVersionName.value = ""
		}
	}

	private fun checkForUpdate() {
		viewModelScope.launch(Dispatchers.Default) {
			try {
				val newerVersion = updateUtils.checkForUpdate()
				newerVersion?.let { newVersion ->
					_newVersionName.postValue(newVersion.name)
				}
			} catch(e: Exception) {
				Timber.w("Log: checkForUpdate: $e")
			}
		}
	}
}
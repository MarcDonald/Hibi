package com.marcdonald.hibi.mainscreens

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
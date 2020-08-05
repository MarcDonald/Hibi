/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi.screens.settings.updatedialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.network.NoConnectivityException
import com.marcdonald.hibi.data.network.github.GithubRateLimitExceededException
import com.marcdonald.hibi.internal.utils.UpdateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException

class UpdateDialogViewModel(private val updateUtils: UpdateUtils) : ViewModel() {
	private val _displayDismiss = MutableLiveData<Boolean>()
	val displayDismiss: LiveData<Boolean>
		get() = _displayDismiss

	private val _displayOpenButton = MutableLiveData<Boolean>()
	val displayOpenButton: LiveData<Boolean>
		get() = _displayOpenButton

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoConnection = MutableLiveData<Boolean>()
	val displayNoConnection: LiveData<Boolean>
		get() = _displayNoConnection

	private val _displayRateLimitError = MutableLiveData<Boolean>()
	val displayRateLimitError: LiveData<Boolean>
		get() = _displayRateLimitError

	private val _displayNoUpdateAvailable = MutableLiveData<Boolean>()
	val displayNoUpdateAvailable: LiveData<Boolean>
		get() = _displayNoUpdateAvailable

	private val _newVersionName = MutableLiveData<String>()
	val newVersionName: LiveData<String>
		get() = _newVersionName

	private val _displayError = MutableLiveData<Boolean>()
	val displayError: LiveData<Boolean>
		get() = _displayError

	init {
		_displayOpenButton.value = false
		_displayNoConnection.value = false
		_displayNoUpdateAvailable.value = false
		_displayDismiss.value = true
	}

	fun check() {
		viewModelScope.launch(Dispatchers.Default) {
			_displayLoading.postValue(true)
			try {
				val newestVersion = updateUtils.checkForUpdate()
				if(newestVersion != null) {
					_newVersionName.postValue(newestVersion.name)
					_displayOpenButton.postValue(true)
					_displayNoUpdateAvailable.postValue(false)
				} else {
					_displayNoUpdateAvailable.postValue(true)
				}
			} catch(e: GithubRateLimitExceededException) {
				_displayRateLimitError.postValue(true)
			} catch(e: NoConnectivityException) {
				_displayNoConnection.postValue(true)
			} catch(e: Exception) {
				_displayError.postValue(true)
				Timber.e("Log: updateDialogViewModel: check: ${e.message}")
			} finally {
				_displayLoading.postValue(false)
			}
		}
	}
}
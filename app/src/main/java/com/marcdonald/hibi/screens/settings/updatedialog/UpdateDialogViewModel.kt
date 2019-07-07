package com.marcdonald.hibi.screens.settings.updatedialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.internal.NoConnectivityException
import com.marcdonald.hibi.internal.utils.UpdateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
			} catch(e: NoConnectivityException) {
				_displayNoConnection.postValue(true)
			} catch(e: NumberFormatException) {
				_displayError.postValue(true)
			} finally {
				_displayLoading.postValue(false)
			}
		}
	}
}
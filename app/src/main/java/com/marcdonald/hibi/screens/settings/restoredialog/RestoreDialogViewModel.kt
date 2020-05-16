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
package com.marcdonald.hibi.screens.settings.restoredialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.hibi.data.database.AppDatabase
import com.marcdonald.hibi.internal.PRODUCTION_DATABASE_NAME
import com.marcdonald.hibi.internal.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipException
import java.util.zip.ZipFile

class RestoreDialogViewModel(private val fileUtils: FileUtils,
														 private val database: AppDatabase)
	: ViewModel() {

	private var restoreFilePath = ""
	private val _displayMessage = MutableLiveData<Boolean>()
	val displayMessage: LiveData<Boolean>
		get() = _displayMessage

	private val _displayButtons = MutableLiveData<Boolean>()
	val displayButtons: LiveData<Boolean>
		get() = _displayButtons

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayError = MutableLiveData<Boolean>()
	val displayError: LiveData<Boolean>
		get() = _displayError

	private val _displayDismiss = MutableLiveData<Boolean>()
	val displayDismiss: LiveData<Boolean>
		get() = _displayDismiss

	private val _canDismiss = MutableLiveData<Boolean>()
	val canDismiss: LiveData<Boolean>
		get() = _canDismiss

	init {
		_displayButtons.value = true
		_displayMessage.value = true
		_canDismiss.value = true
		_displayLoading.value = false
		_displayError.value = false
		_displayDismiss.value = false
	}

	fun passArguments(filePath: String) {
		restoreFilePath = filePath
	}

	fun restore() {
		viewModelScope.launch(Dispatchers.IO) {
			_displayLoading.postValue(true)
			_displayMessage.postValue(false)
			_displayButtons.postValue(false)
			_displayDismiss.postValue(false)
			_canDismiss.postValue(false)
			val restoreSuccess = attemptRestore()
			_displayLoading.postValue(false)
			if(!restoreSuccess) {
				_displayError.postValue(true)
				_displayDismiss.postValue(true)
				_canDismiss.postValue(true)
			}
		}
	}

	private fun attemptRestore(): Boolean {
		return if(isValid()) {
			performRestore()
			true
		} else {
			false
		}
	}

	private fun isValid(): Boolean {
		if(restoreFilePath.isBlank()) {
			Timber.e("Log: isValid: No restore file path")
			return false
		}

		try {
			val file = ZipFile(restoreFilePath)
			for(element in file.entries()) {
				if(element.name == PRODUCTION_DATABASE_NAME)
					return true
			}
		} catch(e: ZipException) {
			Timber.e("Log: isValid: $e")
		}
		return false
	}

	private fun performRestore() {
		File(fileUtils.imagesDirectory).mkdirs()
		clearCurrentImages()
		moveImagesFromBackupToCurrent()
		clearCurrentDatabase()
		moveDatabase()
	}

	private fun clearCurrentImages() {
		val artworkDirectory = File(fileUtils.imagesDirectory)
		for(file in artworkDirectory.listFiles()) {
			file.delete()
		}
	}

	private fun moveImagesFromBackupToCurrent() {
		val file = ZipFile(restoreFilePath)
		file.entries().asSequence().forEach { entry ->
			if(entry.toString() != PRODUCTION_DATABASE_NAME) {
				val inputStream = file.getInputStream(entry)
				val outFile = File(fileUtils.imagesDirectory + entry.toString())
				val outputStream = FileOutputStream(outFile)
				inputStream.copyTo(outputStream)
				inputStream.close()
				outputStream.close()
			}
		}
	}

	private fun clearCurrentDatabase() {
		database.closeDB()
		File(fileUtils.databaseDirectory).delete()
	}

	private fun moveDatabase() {
		val file = ZipFile(restoreFilePath)
		file.entries().asSequence().forEach { entry ->
			if(entry.toString() == PRODUCTION_DATABASE_NAME) {
				val inputStream = file.getInputStream(entry)
				val outFile = File(fileUtils.databaseDirectory)
				val outputStream = FileOutputStream(outFile)
				inputStream.copyTo(outputStream)
				inputStream.close()
				outputStream.close()
			}
		}
		/* This is apparently bad practice but I can't find any other way of completely destroying
		 * the application so that the database can be opened again */
		System.exit(1)
	}
}
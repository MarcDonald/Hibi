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
package com.marcdonald.hibi.screens.entryscreens.addentryscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.*
import com.marcdonald.hibi.internal.utils.FileUtils

class AddEntryViewModelFactory(private val application: Application,
															 private val entryRepository: EntryRepository,
															 private val tagEntryRelationRepository: TagEntryRelationRepository,
															 private val bookEntryRelationRepository: BookEntryRelationRepository,
															 private val newWordRepository: NewWordRepository,
															 private val entryImageRepository: EntryImageRepository,
															 private val fileUtils: FileUtils)
	: ViewModelProvider.AndroidViewModelFactory(application) {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return AddEntryViewModel(application, entryRepository, tagEntryRelationRepository, bookEntryRelationRepository, newWordRepository, entryImageRepository, fileUtils) as T
	}
}
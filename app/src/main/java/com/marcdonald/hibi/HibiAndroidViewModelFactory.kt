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
package com.marcdonald.hibi

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.repository.*
import com.marcdonald.hibi.internal.utils.DateTimeUtils
import com.marcdonald.hibi.internal.utils.FileUtils
import com.marcdonald.hibi.internal.utils.UpdateUtils
import com.marcdonald.hibi.screens.entryscreens.addentryscreen.AddEntryViewModel
import com.marcdonald.hibi.screens.mainscreen.MainScreenViewModel

@Suppress("UNCHECKED_CAST")
class HibiAndroidViewModelFactory(private val application: Application,
																	private val entryRepository: EntryRepository,
																	private val tagEntryRelationRepository: TagEntryRelationRepository,
																	private val newWordRepository: NewWordRepository,
																	private val bookEntryRelationRepository: BookEntryRelationRepository,
																	private val entryImageRepository: EntryImageRepository,
																	private val fileUtils: FileUtils,
																	private val updateUtils: UpdateUtils,
																	private val dateTimeUtils: DateTimeUtils
) : ViewModelProvider.NewInstanceFactory() {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return with(modelClass) {
			when {
				isAssignableFrom(MainScreenViewModel::class.java) -> MainScreenViewModel(application, updateUtils)
				isAssignableFrom(AddEntryViewModel::class.java)   -> AddEntryViewModel(application, entryRepository, tagEntryRelationRepository, bookEntryRelationRepository, newWordRepository, entryImageRepository, fileUtils, dateTimeUtils)
				else                                              -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
			}
		} as T
	}
}
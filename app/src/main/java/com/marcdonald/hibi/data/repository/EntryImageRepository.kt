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
package com.marcdonald.hibi.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.hibi.data.entity.EntryImage

interface EntryImageRepository {

	suspend fun addEntryImage(entryImage: EntryImage)

	suspend fun deleteEntryImage(entryImage: EntryImage)

	fun getImagesForEntry(entryId: Int): LiveData<List<EntryImage>>

	suspend fun countUsesOfImage(imageName: String): Int

	fun getCountImagesForEntry(entryId: Int): LiveData<Int>
}
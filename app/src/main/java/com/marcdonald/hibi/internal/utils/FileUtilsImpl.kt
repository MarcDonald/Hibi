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
package com.marcdonald.hibi.internal.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.marcdonald.hibi.internal.PACKAGE
import com.marcdonald.hibi.internal.PRODUCTION_DATABASE_NAME
import timber.log.Timber
import java.io.File

class FileUtilsImpl(private val context: Context) : FileUtils {
	override fun deleteImage(imageName: String) {
		val filePath = imagesDirectory + imageName
		val file = File(filePath)
		if(file.exists()) {
			file.delete()
		} else {
			Timber.w("Log: deleteImage: File doesn't exist")
		}
	}

	override fun saveImage(file: File) {
		val toPath = imagesDirectory + file.name
		val toFile = File(toPath)
		if(toFile.compareTo(file) != 0) {
			try {
				file.copyTo(toFile, true)
			} catch(e: NoSuchFileException) {
				Timber.e("Log: saveImage: $e")
			}
		} else {
			Timber.d("Log: saveImage: No need to save as file already exists in storage")
		}
	}

	override val imagesDirectory: String
		get() = context.filesDir.path + "/images/"

	override fun getUriForFilePath(filePath: String): Uri {
		val file = File(filePath)
		return FileProvider.getUriForFile(context, "$PACKAGE.FileProvider", file)
	}

	override val localBackupDirectory: String
		get() = context.filesDir.path + "/backup/"

	override val databaseDirectory: String
		get() = context.getDatabasePath(PRODUCTION_DATABASE_NAME).path
}
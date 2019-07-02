package com.marcdonald.hibi.internal.utils

import android.net.Uri
import java.io.File

interface FileUtils {
	fun deleteImage(imageName: String)

	fun saveImage(file: File)

	fun getUriForFilePath(filePath: String): Uri

	val imagesDirectory: String

	val localBackupDirectory: String

	val databaseDirectory: String
}
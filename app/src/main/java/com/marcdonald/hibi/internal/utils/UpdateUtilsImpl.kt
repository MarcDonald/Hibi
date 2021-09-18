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
package com.marcdonald.hibi.internal.utils

import com.marcdonald.hibi.BuildConfig
import com.marcdonald.hibi.data.network.github.GithubAPIService
import com.marcdonald.hibi.data.network.github.apiresponse.GithubVersionResponse

class UpdateUtilsImpl(private val githubAPIService: GithubAPIService) : UpdateUtils {

	override suspend fun checkForUpdate(): GithubVersionResponse? {
		val newestVersion = githubAPIService.getNewestVersion()
		val semicolonIndex = newestVersion.tagName.indexOf(';')
		val newestVersionCode = newestVersion.tagName.substring(semicolonIndex + 1)
		return if(newestVersionCode.toInt() > BuildConfig.VERSION_CODE) {
			newestVersion
		} else {
			null
		}
	}
}
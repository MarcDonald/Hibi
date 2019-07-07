package com.marcdonald.hibi.internal.utils

import com.marcdonald.hibi.BuildConfig
import com.marcdonald.hibi.data.network.github.GithubAPIService
import com.marcdonald.hibi.data.network.github.apiresponse.GithubVersionResponse

class UpdateUtilsImpl(private val githubAPIService: GithubAPIService) : UpdateUtils {

	override suspend fun checkForUpdate(): GithubVersionResponse? {
		val newestVersion = githubAPIService.getNewestVersion()
		val semicolonIndex = newestVersion.tag_name.indexOf(';')
		val newestVersionCode = newestVersion.tag_name.substring(semicolonIndex + 1)
		return if(newestVersionCode.toInt() > BuildConfig.VERSION_CODE) {
			newestVersion
		} else {
			null
		}
	}
}
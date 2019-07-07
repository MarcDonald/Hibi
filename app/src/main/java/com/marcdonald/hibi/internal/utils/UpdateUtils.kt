package com.marcdonald.hibi.internal.utils

import com.marcdonald.hibi.data.network.github.apiresponse.GithubVersionResponse

interface UpdateUtils {
	suspend fun checkForUpdate(): GithubVersionResponse?
}
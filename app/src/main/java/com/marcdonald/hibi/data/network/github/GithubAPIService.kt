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
package com.marcdonald.hibi.data.network.github

import com.marcdonald.hibi.data.network.ConnectivityInterceptor
import com.marcdonald.hibi.data.network.github.apiresponse.GithubVersionResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GithubAPIService {

	@GET("repos/MarcDonald/Hibi/releases/latest")
	suspend fun getNewestVersion(): GithubVersionResponse

	companion object {
		operator fun invoke(connectivityInterceptor: ConnectivityInterceptor, githubStatusCodeInterceptor: GithubStatusCodeInterceptor): GithubAPIService {
			val okHttpClient = OkHttpClient.Builder()
				.addInterceptor(connectivityInterceptor)
				.addInterceptor(githubStatusCodeInterceptor)
				.build()

			return Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(GithubAPIService::class.java)
		}
	}
}
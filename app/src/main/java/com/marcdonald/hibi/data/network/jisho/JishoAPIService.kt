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
package com.marcdonald.hibi.data.network.jisho

import com.marcdonald.hibi.data.network.ConnectivityInterceptor
import com.marcdonald.hibi.data.network.jisho.apiresponse.SearchResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface JishoAPIService {

	@GET("words")
	suspend fun searchTerm(@Query("keyword") searchTerm: String): SearchResponse

	companion object {
		operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): JishoAPIService {
			val okHttpClient = OkHttpClient.Builder()
				.addInterceptor(connectivityInterceptor)
				.build()

			val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

			return Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl("https://jisho.org/api/v1/search/")
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.build()
				.create(JishoAPIService::class.java)
		}
	}
}
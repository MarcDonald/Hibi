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
    operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): GithubAPIService {
      val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(connectivityInterceptor)
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
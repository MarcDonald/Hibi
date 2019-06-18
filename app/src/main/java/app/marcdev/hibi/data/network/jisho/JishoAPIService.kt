package app.marcdev.hibi.data.network.jisho

import app.marcdev.hibi.data.network.ConnectivityInterceptor
import app.marcdev.hibi.data.network.jisho.apiresponse.SearchResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

      return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://jisho.org/api/v1/search/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JishoAPIService::class.java)
    }
  }
}
package app.marcdev.nikki.data

import app.marcdev.nikki.data.apiresponse.SearchResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface JishoAPIService {

  @GET("words")
  fun searchWord(@Query("keyword") searchTerm: String): Deferred<SearchResponse>

  companion object {
    operator fun invoke(): JishoAPIService {
      val okHttpClient = OkHttpClient.Builder().build()
      return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://jisho.org/api/v1/search/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JishoAPIService::class.java)
    }
  }
}
package com.example.forecasty.source
import com.example.forecasty.pojo.QuoteModel
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://api.quotable.io/"


var retrofit1: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


 interface QuoteInterface {
     @GET("random")
     suspend fun getRandomQuote():Response<QuoteModel>
}

object QuoteService {
    val retrofitQuoteService: QuoteInterface by lazy {
        retrofit1.create(QuoteInterface::class.java)
    }
}

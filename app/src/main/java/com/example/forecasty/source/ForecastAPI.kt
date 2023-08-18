package com.example.forecasty.source
import com.example.forecasty.pojo.BaseModel
import com.example.forecasty.pojo.forecast.FiveDaysForecastModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

const val timeoutDuration = 5L // Timeout duration in seconds
val client: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(timeoutDuration, TimeUnit.SECONDS)
    .readTimeout(timeoutDuration, TimeUnit.SECONDS)
    .writeTimeout(timeoutDuration, TimeUnit.SECONDS)
    .build()

var retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()


 interface OpenWeather {
     @GET("weather")
     suspend fun getCurrentWeather(
         @Query("lat") lat: String,
         @Query("lon") lon: String,
         @Query("appid") apiKey: String,
         @Query("units") units: String
     ): Response<BaseModel>

     @GET("forecast")
     suspend fun get3hourlyFiveDaysForecastData(
         @Query("lat") lat: String,
         @Query("lon") lon: String,
         @Query("appid") apiKey: String,
         @Query("units") units: String
     ): Response<FiveDaysForecastModel>
}

object ApiService {
    val retrofitService: OpenWeather by lazy {
        retrofit.create(OpenWeather::class.java)
    }
}

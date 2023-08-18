package com.example.forecasty.repository

import com.example.forecasty.pojo.BaseModel
import com.example.forecasty.pojo.QuoteModel
import com.example.forecasty.pojo.forecast.FiveDaysForecastModel
import com.example.forecasty.source.ApiService
import com.example.forecasty.source.QuoteService
import com.example.forecasty.source.room.Location
import com.example.forecasty.source.room.LocationDao


class ForecastRepository(private val locationDao: LocationDao){

    //Room database
    val readAllSavedFavorites = locationDao.readAllFavorites()

    suspend fun addNewFavorite(location: Location){
        locationDao.addNewFavorite(location)
    }

    suspend fun deleteFavLocation(location: Location){
        locationDao.deleteLocation(location)
    }


    //API
    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        apiKey: String,
        units: String = "metric"
    ): BaseModel? {
        val response = ApiService.retrofitService.getCurrentWeather(lat, lon, apiKey, units)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun get3hourlyFiveDaysForecastData(
        lat: String,
        lon: String,
        apiKey: String,
        units: String = "metric"
    ): FiveDaysForecastModel? {
        val response = ApiService.retrofitService.get3hourlyFiveDaysForecastData(lat, lon, apiKey, units)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }


    suspend fun getRandomQuote():QuoteModel?{
        val response = QuoteService.retrofitQuoteService.getRandomQuote()
        if (response.isSuccessful){
            return response.body()
        }
        return null
    }
}
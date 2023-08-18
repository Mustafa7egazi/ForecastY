package com.example.forecasty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecasty.pojo.BaseModel
import com.example.forecasty.pojo.QuoteModel
import com.example.forecasty.pojo.forecast.FiveDaysForecastModel
import com.example.forecasty.repository.ForecastRepository
import com.example.forecasty.source.room.Location
import com.example.forecasty.source.room.LocationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class ForecastViewModel(application: Application):AndroidViewModel(application){

    private val repo:ForecastRepository
    val readAllSavedFavorites:LiveData<List<Location>>


    init {
        val locationDao = LocationDatabase.getDatabase(application).dataAccessObject()
        repo = ForecastRepository(locationDao)
        readAllSavedFavorites = repo.readAllSavedFavorites
    }

    private val _gettingWeatherStatus = MutableLiveData<BaseModel?>()
    val gettingWeatherStatus:LiveData<BaseModel?>
        get() = _gettingWeatherStatus

    private val _gettingFiveDaysForecastStatus = MutableLiveData<FiveDaysForecastModel?>()
    val gettingFiveDaysForecastStatus:LiveData<FiveDaysForecastModel?>
        get() = _gettingFiveDaysForecastStatus


    private val _gettingQuoteStatus = MutableLiveData<QuoteModel?>()
    val gettingQuoteStatus:LiveData<QuoteModel?>
        get() = _gettingQuoteStatus




    fun addNewFavorite(location: Location){
        viewModelScope.launch(Dispatchers.IO){
            repo.addNewFavorite(location)
        }
    }

    fun deleteFavLocation(location: Location){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteFavLocation(location)
        }
    }



     fun getCurrentWeather(
        lat: String,
        lon: String,
        apiKey: String,
        units: String = "metric"
    ){
         viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repo.getCurrentWeather(lat,lon,apiKey,units)
                withContext(Dispatchers.Main){
                    _gettingWeatherStatus.value = result
                }
            }catch (e:SocketTimeoutException){
                withContext(Dispatchers.Main){
                    _gettingWeatherStatus.value = null
                }
            }
         }
     }

    fun get3hourlyFiveDaysForecastData(
        lat: String,
        lon: String,
        apiKey: String,
        units: String = "metric"
    ){
         viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repo.get3hourlyFiveDaysForecastData(lat,lon,apiKey,units)
                withContext(Dispatchers.Main){
                    _gettingFiveDaysForecastStatus.value = result
                }
            }catch (e:SocketTimeoutException){
                withContext(Dispatchers.Main){
                    _gettingFiveDaysForecastStatus.value = null
                }
            }
         }
     }


    fun getRandomQuote(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repo.getRandomQuote()
                withContext(Dispatchers.Main){
                    _gettingQuoteStatus.value = result
                }
            }catch (e:SocketTimeoutException){
                withContext(Dispatchers.Main){
                    _gettingQuoteStatus.value = null
                }
            }
        }
    }
}
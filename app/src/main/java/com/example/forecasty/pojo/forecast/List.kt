package com.example.forecasty.pojo.forecast

import com.example.forecasty.pojo.Clouds
import com.example.forecasty.pojo.Main
import com.example.forecasty.pojo.Sys
import com.example.forecasty.pojo.Weather
import com.example.forecasty.pojo.Wind
import com.google.gson.annotations.SerializedName
import kotlin.collections.List


data class List (

	@SerializedName("dt") val dt : Any,
	@SerializedName("main") val main : Main,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("clouds") val clouds : Clouds,
	@SerializedName("wind") val wind : Wind,
	@SerializedName("visibility") val visibility : Any,
	@SerializedName("pop") val pop : Any,
	@SerializedName("sys") val sys : Sys,
	@SerializedName("dt_txt") val dt_txt : String
)
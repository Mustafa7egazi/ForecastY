package com.example.forecasty.pojo.forecast

import com.google.gson.annotations.SerializedName
import kotlin.collections.List


data class FiveDaysForecastModel(
	@SerializedName("cod") val cod : Int,
	@SerializedName("message") val message : Int,
	@SerializedName("cnt") val cnt : Int,
	@SerializedName("list") val list : List<com.example.forecasty.pojo.forecast.List>,
	@SerializedName("city") val city : City
)

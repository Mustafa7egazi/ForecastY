package com.example.forecasty.pojo

import com.google.gson.annotations.SerializedName

data class Main (

	@SerializedName("temp") val temp : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("temp_min") val tempMin : Double,
	@SerializedName("temp_max") val tempMax : Double,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int,
	@SerializedName("sea_level") val seaLevel : Int,
	@SerializedName("grnd_level") val groundLevel : Int
)
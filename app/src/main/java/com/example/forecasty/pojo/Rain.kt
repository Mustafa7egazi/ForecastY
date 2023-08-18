package com.example.forecasty.pojo

import com.google.gson.annotations.SerializedName

data class Rain (
	@SerializedName("1h") val oneHour : Double
)
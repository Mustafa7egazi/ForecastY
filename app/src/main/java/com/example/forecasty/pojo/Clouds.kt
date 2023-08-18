package com.example.forecasty.pojo

import com.google.gson.annotations.SerializedName

data class Clouds (
	@SerializedName("all") val all : Int
)
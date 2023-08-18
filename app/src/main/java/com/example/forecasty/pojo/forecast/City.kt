package com.example.forecasty.pojo.forecast

import com.example.forecasty.pojo.Coord
import com.google.gson.annotations.SerializedName


data class City (

	@SerializedName("id") val id : Any,
	@SerializedName("name") val name : String,
	@SerializedName("coord") val coord : Coord,
	@SerializedName("country") val country : String,
	@SerializedName("population") val population : Any,
	@SerializedName("timezone") val timezone : Any,
	@SerializedName("sunrise") val sunrise : Any,
	@SerializedName("sunset") val sunset : Any
)
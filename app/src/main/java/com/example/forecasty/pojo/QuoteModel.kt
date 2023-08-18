package com.example.forecasty.pojo

import com.google.gson.annotations.SerializedName


data class QuoteModel (
	@SerializedName("_id") val _id : String,
	@SerializedName("content") val content : String,
	@SerializedName("author") val author : String,
	@SerializedName("tags") val tags : List<String>,
	@SerializedName("authorSlug") val authorSlug : String,
	@SerializedName("length") val length : Int,
	@SerializedName("dateAdded") val dateAdded : String,
	@SerializedName("dateModified") val dateModified : String
)
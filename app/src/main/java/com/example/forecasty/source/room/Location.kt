package com.example.forecasty.source.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val lat:String,
    val lon:String,
    val address:String
)

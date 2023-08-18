package com.example.forecasty.ui.main_content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.forecasty.R
import com.example.forecasty.databinding.ActivityContentBinding
import com.example.forecasty.viewmodel.ForecastViewModel

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    lateinit var address: String
    lateinit var lat: String
    lateinit var lon: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent != null) {
            address =  intent.getStringExtra("address").toString()
            lat = intent.getStringExtra("lat").toString()
            lon= intent.getStringExtra("lon").toString()
        }
        else{
            Toast.makeText(this,"Null intent data",Toast.LENGTH_SHORT).show()
        }
    }
}
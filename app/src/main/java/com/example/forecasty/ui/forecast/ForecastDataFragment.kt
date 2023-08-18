package com.example.forecasty.ui.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.forecasty.R
import com.example.forecasty.adapter.ForecastAdapter
import com.example.forecasty.databinding.FragmentForecastDataBinding
import com.example.forecasty.viewmodel.ForecastViewModel

class ForecastDataFragment : Fragment() {
    private lateinit var binding: FragmentForecastDataBinding
    private val viewModel:ForecastViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_forecast_data, container, false)

        val args:ForecastDataFragmentArgs by navArgs()
        val lat = args.lat
        val lon = args.lon
        val address = args.address
        binding.currentLocationTV.text = address

        viewModel.get3hourlyFiveDaysForecastData(lat,lon,getString(R.string.api_key))
        viewModel.gettingFiveDaysForecastStatus.observe(viewLifecycleOwner){forecastData->
            if (forecastData!= null){
                val forecastAdapter = ForecastAdapter(this).apply {
                    submitList(forecastData.list)
                }
                binding.forecastDataRecycler.adapter = forecastAdapter
            }else{
                val action = ForecastDataFragmentDirections.actionForecastDataFragmentToConnectionErrorFragment("api")
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

}
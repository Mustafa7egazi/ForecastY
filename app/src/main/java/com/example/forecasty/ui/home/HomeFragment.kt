package com.example.forecasty.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.forecasty.R
import com.example.forecasty.databinding.FragmentHomeBinding
import com.example.forecasty.source.room.Location
import com.example.forecasty.ui.main_content.ContentActivity
import com.example.forecasty.util.getCountryFullName
import com.example.forecasty.util.getTheDateFromTimestamp
import com.example.forecasty.util.getTimeFromUnixTimestamp
import com.example.forecasty.util.isInternetAvailable
import com.example.forecasty.viewmodel.ForecastViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ForecastViewModel by viewModels()

    private var latFromMap: String? = null
    private var lonFromMap: String? = null
    private var addressFromMap: String? = null

    private var lat: String? = null
    private var lon: String? = null
    private var address: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        val hostActivity = requireActivity() as ContentActivity
        lat = hostActivity.lat
        lon = hostActivity.lon
        address = hostActivity.address

        val weatherApiKey = getString(R.string.api_key)

        try {
            val args: HomeFragmentArgs by navArgs()
            latFromMap = args.lat
            lonFromMap = args.lon

            if (isInternetAvailable(requireContext())) {
                if (latFromMap == null && lonFromMap == null) {
                    showDataOnScreen(lat!!, lon!!, weatherApiKey, address!!)
                } else {
                    showDataOnScreen(latFromMap!!, lonFromMap!!, weatherApiKey, "getFromApi")
                }
            } else {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToConnectionErrorFragment("internet")
                findNavController().navigate(action)
            }
        } catch (e: Exception) {
            if (isInternetAvailable(requireContext())) {
                showDataOnScreen(lat!!, lon!!, weatherApiKey, address!!)
            } else {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToConnectionErrorFragment("internet")
                findNavController().navigate(action)
            }
        }

        val popupMenu = PopupMenu(requireContext(), binding.favoriteTV)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_items, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            dealWithDatabase(menuItem.itemId)
            true
        }

        binding.favoriteTV.setOnClickListener {
            popupMenu.show()
        }


        binding.useMapTextBtn.setOnClickListener {
            val bundle = Bundle().also {
                it.putString("lat", lat)
                it.putString("lon", lon)
                it.putString("address", address)
            }
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment2, bundle)
        }

        binding.fiveDaysForecastTV.setOnClickListener {
            val argLat:String
            val argLon:String
            val argAddress:String
            if (latFromMap == null && lonFromMap == null){
                argLat = lat!!
                argLon = lon!!
                argAddress = address!!
            }else{
                argLat = latFromMap!!
                argLon = latFromMap!!
                argAddress = addressFromMap!!
            }

            val action = HomeFragmentDirections.actionHomeFragmentToForecastDataFragment(argLat,argLon,argAddress)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun dealWithDatabase(itemId: Int) {

        when (itemId) {
            R.id.saveToDatabase -> addNewItemToDatabase()
            R.id.showFromDatabase -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
            }
        }

    }

    private fun addNewItemToDatabase() {
        val location: Location = if (latFromMap == null && lonFromMap == null) {
            Location(0, lat!!, lon!!, address!!)
        } else {
            Location(0, latFromMap!!, lonFromMap!!, addressFromMap!!)
        }
        viewModel.addNewFavorite(location)
        Toast.makeText(requireContext(), "Successfully saved to favorites!", Toast.LENGTH_SHORT)
            .show()

    }

    @SuppressLint("SetTextI18n")
    private fun showDataOnScreen(lat: String, lon: String, apiKey: String, address: String) {
        viewModel.getRandomQuote()

        viewModel.getCurrentWeather(lat, lon, apiKey)

        //getting quote
        viewModel.gettingQuoteStatus.observe(viewLifecycleOwner) { quote ->
            if (quote != null) {
                binding.quoteHeader.text = quote.tags[0]
                binding.theQuoteBody.text = quote.content
                binding.authorNameTV.text = "-${quote.author}"

            } else {
                binding.quoteHeader.visibility = View.GONE
                binding.theQuoteBody.visibility = View.GONE
                binding.authorNameTV.visibility = View.GONE
            }
        }

        //getting weather
        viewModel.gettingWeatherStatus.observe(viewLifecycleOwner) { weatherData ->
            if (weatherData != null) {
                when (weatherData.weather[0].id) {
                    in 200..232 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_thunderstorm_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.thunderstorm_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.thunderstormColor
                            )
                        )
                    }

                    in 300..321 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_drizzle_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.drizzle_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.drizzleColor
                            )
                        )
                    }

                    in 500..531 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_rainy_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.rainy_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainyColor
                            )
                        )
                    }

                    in 600..622 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_snowy_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.snowy_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.snowyColor
                            )
                        )
                    }

                    in 701..781 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_cloudy_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.cloudy_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyColor
                            )
                        )
                    }

                    800 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_sunny_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.sunny_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunnyColor
                            )
                        )
                    }

                    in 801..804 -> {
                        binding.parentLayout.setBackgroundResource(R.drawable.z_cloudy_background)
                        binding.imageViewOfWeatherIcon.setImageResource(R.drawable.cloudy_icon)
                        binding.todayTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.dateTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.addressTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.weatherStatusTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.currentWeatherDegreeTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.feelsLikeAndSunsetTV.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyTextColor
                            )
                        )
                        binding.headerMaterialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudyColor
                            )
                        )
                    }
                }


                if (address == "getFromApi") {
                    if (weatherData.sys.country != null) {
                        binding.addressTV.text =
                            "${weatherData.name}, ${getCountryFullName(weatherData.sys.country.uppercase())}"

                        addressFromMap = binding.addressTV.text.toString()
                    } else {
                        binding.addressTV.text =
                            "${weatherData.name}, no country name"
                        addressFromMap = binding.addressTV.text.toString()
                    }
                } else {
                    if (weatherData.sys.country != null) {
                        binding.addressTV.text =
                            "$address, ${getCountryFullName(weatherData.sys.country.uppercase())}"
                    } else {
                        binding.addressTV.text = address
                    }
                }

                binding.dateTV.text = getTheDateFromTimestamp(weatherData.dt.toLong())
                binding.currentWeatherDegreeTV.text = "${weatherData.main.temp}°"
                binding.weatherStatusTV.text = weatherData.weather[0].main
                binding.feelsLikeAndSunsetTV.text =
                    "Feels like ${weatherData.main.feels_like} | Sunset ${
                        getTimeFromUnixTimestamp(
                            weatherData.sys.sunset.toLong()
                        )
                    }"
                binding.pressureTV.text = "${weatherData.main.pressure} hpa\nPressure"
                binding.windSpeedTV.text = "${weatherData.wind.speed} km/h\nWind speed"
                binding.humidityTV.text = "${weatherData.main.humidity}%\nHumidity"
                binding.visibilityTV.text = "${(weatherData.visibility) / 1000} km\nVisibility"

                binding.minTempTV.text = "${weatherData.main.tempMin}°\nMin Temp"
                binding.maxTempTV.text = "${weatherData.main.tempMax}°\nMax Temp"



                binding.headerMaterialCardView.visibility = View.VISIBLE
                binding.allStatusMaterialCardView.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE


            } else {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToConnectionErrorFragment("api")
                findNavController().navigate(action)
            }

        }
    }

}
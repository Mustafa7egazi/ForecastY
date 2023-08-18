package com.example.forecasty.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.forecasty.R
import com.example.forecasty.adapter.PlacesAdapter
import com.example.forecasty.databinding.FragmentFavoritesBinding
import com.example.forecasty.viewmodel.ForecastViewModel


class FavoritesFragment : Fragment() {

    private lateinit var binding:FragmentFavoritesBinding
    private val viewModel: ForecastViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_favorites, container, false)

        viewModel.readAllSavedFavorites.observe(viewLifecycleOwner){placesList->
           if (!placesList.isNullOrEmpty()){
              val placesAdapter = PlacesAdapter(this,viewModel).also {
                  it.submitList(placesList)
              }
               binding.favPlacesRecycler.adapter = placesAdapter
           }else{
               binding.favPlacesRecycler.visibility = View.GONE
               binding.favHeaderTV.text = "No saved places yet!"
           }
        }

        return binding.root
    }
}

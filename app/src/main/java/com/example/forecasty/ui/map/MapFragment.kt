package com.example.forecasty.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.forecasty.R
import com.example.forecasty.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var lat:String
    private lateinit var lon:String
    private lateinit var address:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

        lat = requireArguments().getString("lat")!!
        lon = requireArguments().getString("lon")!!
        address = requireArguments().getString("address")!!

        return binding.root
    }

    override fun onMapReady(theMap: GoogleMap) {
        mGoogleMap = theMap

        val latLng = LatLng(lat.toDouble(), lon.toDouble())
        zoomOnMap(latLng)

        mGoogleMap.addMarker(
            MarkerOptions()
            .position(latLng)
            .title(address))

        mGoogleMap.setOnMapClickListener { clickedLatLng ->
            mGoogleMap.clear()

            mGoogleMap.addMarker(
                MarkerOptions()
                    .position(clickedLatLng)
                    .title("Clicked location"))

//            val bundle = Bundle().also {
//                it.putString("lat",clickedLatLng.latitude.toString())
//                it.putString("lon",clickedLatLng.longitude.toString())
//                it.putString("address","getFromApi")
//            }

            val action = MapFragmentDirections.actionMapFragment2ToHomeFragment(
                 clickedLatLng.latitude.toString(),
                 clickedLatLng.longitude.toString()
            )
            findNavController().navigate(action)

            Log.d("7egzz", "onMapReady: $clickedLatLng ")
        }
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newMapCam = CameraUpdateFactory.newLatLngZoom(latLng,8f)
        mGoogleMap.animateCamera(newMapCam)
    }

}
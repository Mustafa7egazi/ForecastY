package com.example.forecasty.ui.splash

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.forecasty.databinding.ActivityMainBinding
import com.example.forecasty.ui.main_content.ContentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lat: String
    private lateinit var lon: String
    private var count = 0
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
    }

    override fun onRestart() {
        super.onRestart()
        getCurrentLocation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        getCurrentLocation()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getCurrentLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                // get lat long here
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    count++
                    if (location == null) {
                        Toast.makeText(this, "Relocating..", Toast.LENGTH_SHORT).show()
                        if (count < 3 ){
                            getCurrentLocation()
                        }
                        return@addOnCompleteListener
                    } else {

                        val geocoder = Geocoder(this, Locale.getDefault())

                        var possibleAddress = "No specific address"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(
                                location.latitude, location.longitude, 1
                            ) {
                                possibleAddress =
                                    it.first().subAdminArea + ", " + it.first().adminArea.split(" ")
                                        .first()
                            }
                        } else {
                            val address =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            possibleAddress =
                                address?.first()!!.subAdminArea + ", " + address.first()!!.adminArea.split(
                                    " "
                                ).first()
                        }

                        lat = location.latitude.toString()
                        lon = location.longitude.toString()


                        // Delay for seconds before launching the MainActivity
                        val delayMillis = 2200L
                        findViewById<View>(android.R.id.content).postDelayed({
                            Intent(this@MainActivity , ContentActivity::class.java).also {
                                it.putExtra("address",possibleAddress)
                                it.putExtra("lat",lat)
                                it.putExtra("lon",lon)
                                startActivity(it)
                            }
                            finish()
                        }, delayMillis)
                    }
                }

            } else {
                //settings open here
                showAlertDialog()
            }

        } else {
            //request permissions
            requestPermission()

        }
    }
    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage("You need to enable GPS to find your location")
            .setCancelable(false)
            .setPositiveButton("Go settings") { dialog, _ ->
                // Positive button click listener
                Toast.makeText(this, "Turn on location service!", Toast.LENGTH_SHORT).show()
                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).also {
                    startActivity(it)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Dismiss") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


}
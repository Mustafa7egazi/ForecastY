package com.example.forecasty.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasty.R
import com.example.forecasty.databinding.SavedLocationLayoutBinding
import com.example.forecasty.source.room.Location
import com.example.forecasty.ui.favorites.FavoritesFragment
import com.example.forecasty.ui.favorites.FavoritesFragmentDirections
import com.example.forecasty.viewmodel.ForecastViewModel

class PlacesAdapter(private val fragment: FavoritesFragment,private val viewModel: ForecastViewModel) :
    ListAdapter<Location, PlacesAdapter.ViewHolder>(LocationItemDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SavedLocationLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.savedAddressTV.text = getItem(position).address
        holder.binding.root.setOnClickListener {
            val lat = getItem(position).lat
            val lon = getItem(position).lon
            val navController = NavHostFragment.findNavController(fragment)
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToHomeFragment(lat, lon)
            navController.navigate(action)
        }

        holder.binding.root.setOnLongClickListener {
            val location = Location(
                getItem(position).id,
                getItem(position).lat,
                getItem(position).lon,
                getItem(position).address
            )
            deleteDialog(location, fragment)
            true
        }
    }

    private fun deleteDialog(location: Location, fragment: Fragment) {
        val builder = AlertDialog.Builder(fragment.context).apply {
            setPositiveButton("Yes") { _, _ ->
                deletePlace(location,fragment)
            }

            setNegativeButton("No") { _, _ -> }
            setTitle("Confirm delete")
            setMessage("Are you sure you want to delete ${location.address}?")
        }

        builder.create().show()

    }

    private fun deletePlace(location: Location,fragment: Fragment) {
        viewModel.deleteFavLocation(location)
        Toast.makeText(fragment.context,"Deleted successfully!",Toast.LENGTH_SHORT).show()
        val navController = NavHostFragment.findNavController(fragment)
        navController.navigate(R.id.action_favoritesFragment_to_homeFragment)
    }


    inner class ViewHolder(val binding: SavedLocationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LocationItemDiff : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(
            oldItem: Location, newItem:
            Location
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }
}
package com.example.forecasty.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasty.R
import com.example.forecasty.databinding.ForecastDataItemLayoutBinding
import com.example.forecasty.pojo.forecast.FiveDaysForecastModel
import com.example.forecasty.pojo.forecast.List

class ForecastAdapter(private val fragment:Fragment) : ListAdapter<List, ForecastAdapter.ViewHolder>(ItemDiff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ForecastDataItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.forecastWeatherConditionTV.text = getItem(position).weather[0].main
        holder.binding.forecastWeatherDescriptionTV.text = getItem(position).weather[0].description
        holder.binding.forecastTempTV.text = "${getItem(position).main.temp}°"
        holder.binding.forecastHumidityTV.text = "Humidity: ${getItem(position).main.humidity}%"
        holder.binding.forecastMaxTempTV.text = "Max temp: ${getItem(position).main.tempMax}°"
        holder.binding.forecastMinTempTV.text = "Min temp: ${getItem(position).main.tempMin}°"
        holder.binding.forecastFeelsLikeTV.text =
            "Feels like: ${getItem(position).main.feels_like}°"
        holder.binding.dateAndHourTV.text = getItem(position).dt_txt

        when (getItem(position).weather[0].id) {
            in 200..232 -> {
                holder.binding.dataItemCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.thunderstormTextColor
                    )
                )
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.thunder_animation)
            }

            in 300..321 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.drizzleColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.rainy_animation)
            }

            in 500..531 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.rainyColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.rainy_animation)
            }

            in 600..622 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.snowyColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.snowy_animation)
            }

            800 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.sunnyColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.clear_animation)
            }

            in 801..804 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.cloudyColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.cloudy_animation)
            }

            in 701..781 -> {
                holder.binding.dataItemCard.setBackgroundResource(R.color.cloudyColor)
                holder.binding.forecastTypeAnimation.setAnimation(R.raw.cloudy_animation)
            }
        }

    }


    inner class ViewHolder(val binding: ForecastDataItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemDiff : DiffUtil.ItemCallback<List>() {
        override fun areItemsTheSame(
            oldItem: List, newItem:
            List
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: List,
            newItem: List
        ): Boolean {
            return oldItem == newItem
        }

    }
}
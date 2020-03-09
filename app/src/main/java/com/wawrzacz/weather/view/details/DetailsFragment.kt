package com.wawrzacz.weather.view.details

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wawrzacz.weather.R
import com.wawrzacz.weather.viewmodel.WeatherViewModel
import com.wawrzacz.weather.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.view.*
import java.net.URI
import java.sql.Date
import java.text.SimpleDateFormat

class DetailsFragment: Fragment() {
    private lateinit var viewModel: WeatherViewModel

    // Weather data fields
    private lateinit var cityName: TextView
    private lateinit var temperature: TextView
    private lateinit var icon: ImageView
    private lateinit var description: TextView
    private lateinit var sunriseTime: TextView
    private lateinit var sunsetTime: TextView
    private lateinit var pressure: TextView
    private lateinit var lastUpdateTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val infoButton = view.info_button
        val backButton = view.back_button

        // Initialize fields
        this.cityName = view.city_name
        this.temperature = view.temperature
        this.icon = view.weather_icon
        this.description = view.description
        this.sunriseTime = view.sunrise_hour
        this.sunsetTime = view.sunset_hour
        this.pressure = view.pressure
        this.lastUpdateTime = view.update_time

        // Initialaize ViewModel
        val viewModelFactory = WeatherViewModelFactory()
        this.viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)

        // Add bindings
        addBindings()

        // Add click listeners
        infoButton.setOnClickListener {
            TooltipCompat.setTooltipText(it, "Ostatnia aktualiacja: ${lastUpdateTime.text}")
        }

        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun addBindings() {
        this.viewModel.weatherData.observe(this.viewLifecycleOwner, Observer {
            if (it !== null) {
                this.cityName.text = it.cityName
                this.description.text = it.weather[0].description.capitalize()
                this.icon.setImageResource(getIconIdByCode(it.weather[0].icon))
                this.temperature.text = it.main.temperature.toInt().toString()
                this.sunriseTime.text = formatTimestamp(it.sunData.sunrise, "HH:mm")
                this.sunsetTime.text = formatTimestamp(it.sunData.sunset, "HH:mm")
                this.lastUpdateTime.text = formatTimestamp(it.updateTime, "yyyy-MM-dd HH:mm")
                this.pressure.text = it.main.pressure.toString()
            }
        })
    }

    private fun getIconIdByCode(iconCode: String): Int {
        val nightModeIconCodes = listOf("01", "02")
        val code = iconCode.substring(0, 2)
        val resourceType = "drawable"
        val packageName = "com.wawrzacz.weather"
        val iconName = if (nightModeIconCodes.contains(code)) "w$iconCode" else "w$code"
        val resource = activity?.resources?.getIdentifier(iconName, resourceType, packageName)

        return resource as Int
    }

    private fun formatTimestamp(intTime: Int, dateFormat: String): String {
        val dateFormat = SimpleDateFormat(dateFormat)
        val date = Date(intTime.toLong() * 1000)

        return dateFormat.format(date)
    }
}
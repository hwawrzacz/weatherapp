package com.wawrzacz.weather.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wawrzacz.weather.R
import com.wawrzacz.weather.viewmodel.WeatherViewModel
import com.wawrzacz.weather.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.view.*
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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

        setActionBar()
        initializeFields(view)
        initializeViewModel()
        addBindings()

        val backButton = view.back_button
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun setActionBar() {
        val compatActivity = activity as AppCompatActivity
        val supportActionBar = compatActivity.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initializeFields(view: View) {
        this.cityName = view.city_name
        this.temperature = view.temperature
        this.icon = view.weather_icon
        this.description = view.description
        this.sunriseTime = view.sunrise_hour
        this.sunsetTime = view.sunset_hour
        this.pressure = view.pressure
        this.lastUpdateTime = view.update_time
    }

    private fun initializeViewModel() {
        val viewModelFactory = WeatherViewModelFactory()
        this.viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)
    }

    private fun addBindings() {
        this.viewModel.weatherData.observe(this.viewLifecycleOwner, Observer {
            if (it !== null) {
                this.cityName.text = it.cityName
                this.description.text = it.weather[0].description.capitalize()
                this.icon.setImageResource(getIconIdByCode(it.weather[0].icon))
                this.temperature.text = it.main.temperature.toInt().toString()
                this.sunriseTime.text = timestampToDate(it.sunData.sunrise, "HH:mm")
                this.sunsetTime.text = timestampToDate(it.sunData.sunset, "HH:mm")
                this.lastUpdateTime.text = timestampToDate(it.updateTime, "yyyy-MM-dd HH:mm")
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

    private fun timestampToDate(timestamp: Int, dateFormat: String): String {
        val dateFormatter = SimpleDateFormat(dateFormat)
        val date = Date(timestamp.toLong() * 1000)

        return dateFormatter.format(date)
    }
}

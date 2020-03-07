package com.wawrzacz.weather.view.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wawrzacz.weather.R
import com.wawrzacz.weather.viewmodel.WeatherViewModel
import com.wawrzacz.weather.viewmodel.WeatherViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.view.*

class DetailsFragment: Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var lastUpdateTime: String

    // Weather data fields
    private lateinit var cityName: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val infoButton = view.info_button
        val backButton = view.back_button

        // Initialize fields
        cityName = view.city_name

        // Initialaize ViewModel
        val viewModelFactory = WeatherViewModelFactory()
        viewModel = ViewModelProvider(activity!!, viewModelFactory)
            .get(WeatherViewModel::class.java)

//        viewModel.cityName.value = "OKOKokok"

        // Add bindings
        viewModel.cityName.observe(this.viewLifecycleOwner, Observer {
            Log.i("schab", "observed")
            this.cityName.text = it
        })


        lastUpdateTime = "20.01.2020"
        infoButton.setOnClickListener {
            TooltipCompat.setTooltipText(it, "Ostatnia aktualiacja: $lastUpdateTime")
        }

        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }
}
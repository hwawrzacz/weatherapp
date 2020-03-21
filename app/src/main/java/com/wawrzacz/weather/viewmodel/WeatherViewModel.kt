package com.wawrzacz.weather.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.wawrzacz.weather.data.dal.WeatherRepository
import com.wawrzacz.weather.data.model.WeatherData
import com.wawrzacz.weather.data.model.WeatherDataResponse

class WeatherViewModel: ViewModel() {
    val cityName = MutableLiveData<String>()
    val location = MutableLiveData<Location>()

    private val repository = WeatherRepository.getInstance()
    var weatherData = MutableLiveData<WeatherData>()

    fun getWeatherDataByName(cityName: String): LiveData<WeatherDataResponse>{
        return repository.getWeatherData(cityName)
    }

    fun getWeatherDataByLocation(location: Location): LiveData<WeatherDataResponse>{
        return repository.getWeatherDataByLocation(location)
    }
}

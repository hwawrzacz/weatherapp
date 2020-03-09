package com.wawrzacz.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.wawrzacz.weather.data.dal.WeatherRepository
import com.wawrzacz.weather.data.model.WeatherData
import com.wawrzacz.weather.data.model.WeatherDataResponse

class WeatherViewModel: ViewModel() {
    private val repository = WeatherRepository.getInstance()
    var weatherData = MutableLiveData<WeatherData>()

    fun getWeatherData(cityName: String): LiveData<WeatherDataResponse>{
        return repository.getWeatherData(cityName)
    }
}

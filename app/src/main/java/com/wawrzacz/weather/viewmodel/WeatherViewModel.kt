package com.wawrzacz.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel() {
    val cityName = MutableLiveData<String>()
    val temperature = MutableLiveData<Float>()
    val icon = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val pressure = MutableLiveData<Int>()
    val sunriseTime = MutableLiveData<Int>()
    val sunsetTime = MutableLiveData<Int>()
    val lastUpdate = MutableLiveData<Int>()
}

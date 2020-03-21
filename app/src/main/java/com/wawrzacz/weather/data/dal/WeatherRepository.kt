package com.wawrzacz.weather.data.dal

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.wawrzacz.weather.data.model.WeatherData
import com.wawrzacz.weather.data.model.WeatherDataResponse

class WeatherRepository {
    companion object {
        private var instance: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (instance == null) instance = WeatherRepository()
            return instance as WeatherRepository
        }
    }

    private val weatherApi = WeatherApi()

    fun getWeatherData(cityName: String): MutableLiveData<WeatherDataResponse> {
        return weatherApi.getWeatherData(cityName)
    }
    fun getWeatherDataByLocation(location: Location): MutableLiveData<WeatherDataResponse> {
        return weatherApi.getWeatherDataByLocation(location)
    }
}

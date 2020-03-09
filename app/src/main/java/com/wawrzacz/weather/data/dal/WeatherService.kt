package com.wawrzacz.weather.data.dal

import com.wawrzacz.weather.data.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("weather/?units=metric&lang=pl")
    fun getWeatherData(@Query("q") cityName: String,
                       @Query("appid") apiKey: String): Call<WeatherData>
}
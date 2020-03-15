package com.wawrzacz.weather.data.model

import com.google.gson.annotations.SerializedName

class WeatherData (
    @SerializedName("cod")
    val responseCode: Int,
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: List<Description>,
    @SerializedName("sys")
    val sunData: SunData,
    @SerializedName("dt")
    val updateTime: Int
) {
    override fun toString(): String {
        return "$responseCode | $cityName | ${main.temperature} | ${main.pressure}"
    }
}

class Main (
    @SerializedName("temp")
    val temperature: Float,
    @SerializedName("pressure")
    val pressure: Integer
) {}

class SunData (
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int
) {}

class Description (
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
) {}

package com.wawrzacz.weather.data.model

import com.google.gson.annotations.SerializedName

class WeatherData (
    @SerializedName("name")
    val cityName: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: Iterable<Description>,
    @SerializedName("sys")
    val sunData: SunData,
    @SerializedName("dt")
    val updateTime: Integer
) {}

class Main (
    @SerializedName("temp")
    val temperature: Float,
    @SerializedName("pressure")
    val pressure: Integer
) {}

class SunData (
    @SerializedName("sunrise")
    val sunrise: Integer,
    @SerializedName("sunset")
    val sunset: Integer
) {}

class Description (
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
) {}

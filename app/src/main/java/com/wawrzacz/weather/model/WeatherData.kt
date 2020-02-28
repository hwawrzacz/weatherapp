package com.wawrzacz.weather.model

class WeatherData (
    val cityName: String,
    val temperature: Long,
    val pressure: Int,
    val description: String,
    val sunriseHour: Integer,
    val sunsetHour: Integer,
    val updateTime: Integer) {
}
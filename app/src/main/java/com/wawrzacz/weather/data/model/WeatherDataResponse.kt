package com.wawrzacz.weather.data.model

class WeatherDataResponse (
    var isLoading: Boolean,
    var isFinished: Boolean,
    var isSuccess: Boolean,
    var weatherData: WeatherData?)
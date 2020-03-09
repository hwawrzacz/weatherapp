package com.wawrzacz.weather.data.model

class WeatherDataResponse (
    var isLoading: Boolean,
    var isLoaded: Boolean,
    var isSuccess: Boolean,
    var weatherData: WeatherData?)
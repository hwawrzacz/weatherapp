package com.wawrzacz.weather.data.dal

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.wawrzacz.weather.data.model.WeatherData
import com.wawrzacz.weather.data.model.WeatherDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApi {
    private val URL = "https://api.openweathermap.org/data/2.5/"
    private val API_KEY = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(this.URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        ).build()

    private val service = retrofit.create(WeatherService::class.java)

    fun getWeatherData(cityName: String): MutableLiveData<WeatherDataResponse> {
        val weatherDataResponse = createDefaultWeaterDataResponse()
        var weatherDataRespLiveData = MutableLiveData<WeatherDataResponse>()
        weatherDataRespLiveData.value = weatherDataResponse

        val call = service.getWeatherData(cityName, API_KEY)

        call.enqueue(object: Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val obj:WeatherData? = response.body()
                Log.i("schab:API", obj?.responseCode.toString())
                weatherDataRespLiveData.value = handleOnResponse(response)
            }
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                weatherDataRespLiveData.value = handleOnFailure()
            }
        })

        return weatherDataRespLiveData
    }

    fun getWeatherDataByLocation(location: Location): MutableLiveData<WeatherDataResponse> {
        val weatherDataResponse = createDefaultWeaterDataResponse()
        var weatherDataRespLiveData = MutableLiveData<WeatherDataResponse>()
        weatherDataRespLiveData.value = weatherDataResponse

        val call = service.getWeatherData(location.latitude, location.longitude, API_KEY)

        call.enqueue(object: Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val obj:WeatherData? = response.body()
                Log.i("schab:API", obj?.responseCode.toString())
                weatherDataRespLiveData.value = handleOnResponse(response)
            }
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                weatherDataRespLiveData.value = handleOnFailure()
            }
        })

        return weatherDataRespLiveData
    }

    private fun createDefaultWeaterDataResponse(): WeatherDataResponse {
        return WeatherDataResponse(
            isLoading = true,
            isFinished = false,
            isSuccess = false,
            weatherData = null
        )
    }

    private fun handleOnResponse(response: Response<WeatherData>): WeatherDataResponse{
        val weatherDataResponse = createDefaultWeaterDataResponse()

        if (response.body() === null) {
            weatherDataResponse.isLoading = false
            weatherDataResponse.isFinished = true
            weatherDataResponse.isSuccess = false
        } else if (response?.body()?.responseCode == 200){
            weatherDataResponse.isLoading = false
            weatherDataResponse.isFinished = true
            weatherDataResponse.isSuccess = true
            weatherDataResponse.weatherData = response.body()
        }

        return weatherDataResponse
    }

    private fun handleOnFailure(): WeatherDataResponse {
        val weatherDataResponse = createDefaultWeaterDataResponse()

        weatherDataResponse.isLoading = false
        weatherDataResponse.isFinished = true
        weatherDataResponse.isSuccess = false

        return weatherDataResponse
    }
}
package com.example.weatherapp.ui.`interface`

import com.example.weatherapp.ui.models.WeatherInfo

interface NetworkResponseCallback {
    fun onNetworkSuccess(weatherInfo: WeatherInfo)
    fun onNetworkFailure(message: String)
}
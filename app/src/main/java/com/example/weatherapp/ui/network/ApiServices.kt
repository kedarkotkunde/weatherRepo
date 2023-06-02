package com.example.weatherapp.ui.network

import com.example.weatherapp.ui.models.Country
import com.example.weatherapp.ui.models.WeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("all")
    fun getCountries() : Call<WeatherInfo>

    @GET("/data/2.5/weather")
    fun getWeatherByCity(@Query("q") city : String, @Query("appid") apiKey : String):Call<WeatherInfo>

    @GET("/data/2.5/weather")
    fun getWeatherByCityAndCountrycode(@Query("q") city : String,@Query("q")countryCode : String, @Query("appid") apiKey : String):Call<WeatherInfo>

    @GET("/data/2.5/weather")
    fun getWeatherByFullName(@Query("q") city : String,@Query("q")stateCode : String,@Query("q")countryCode : String, @Query("appid") apiKey : String):Call<WeatherInfo>

}
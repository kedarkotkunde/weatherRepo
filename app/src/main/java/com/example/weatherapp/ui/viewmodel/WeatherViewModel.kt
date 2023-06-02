package com.example.weatherapp.ui.viewmodel

import android.app.Application
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.ui.`interface`.NetworkResponseCallback
import com.example.weatherapp.ui.models.WeatherInfo
import com.example.weatherapp.ui.repositories.CountriesRepository
import com.example.weatherapp.ui.util.Logutil
import com.example.weatherapp.ui.util.NetworkHelper


class WeatherViewModel(private val app: Application) : AndroidViewModel(app) {
     var mList: MutableLiveData<WeatherInfo> = MutableLiveData<WeatherInfo>()
    var test: MutableLiveData<WeatherInfo> = MutableLiveData<WeatherInfo>()

    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var mRepository = CountriesRepository.getInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    fun fetchWeatherFromServer(cityName: String, countryCode : String , stateCode : String): MutableLiveData<WeatherInfo> {

        if (NetworkHelper.isNetworkAvailable(app.baseContext)) {
            mShowProgressBar.value = true
            mList = mRepository!!.getWeather(object : NetworkResponseCallback {
                override fun onNetworkFailure(message: String) {
                    mShowApiError.value =message

                }

                override fun onNetworkSuccess(weatherInfo: WeatherInfo) {
                    mShowProgressBar.value = false
                    test.value = weatherInfo
                  Logutil.d("Network responsde ----> ${weatherInfo.name}")
                }
            }, cityName, countryCode, stateCode)
        } else {
            mShowNetworkError.value = true
        }
        return mList
    }

    fun onRefreshClicked(view: View) {
       // fetchCountriesFromServer(true)
    }
}
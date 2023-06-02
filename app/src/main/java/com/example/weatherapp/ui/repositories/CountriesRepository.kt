package com.example.weatherapp.ui.repositories

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.ui.`interface`.NetworkResponseCallback
import com.example.weatherapp.ui.models.WeatherInfo
import com.example.weatherapp.ui.network.RestClient
import com.example.weatherapp.ui.util.ConstantVal
import com.example.weatherapp.ui.util.Logutil
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountriesRepository private constructor(){

    private lateinit var mCallback: NetworkResponseCallback
    private lateinit var mCountryCall: Call<WeatherInfo>

    private var mCountryList: MutableLiveData<WeatherInfo> =
        MutableLiveData<WeatherInfo>().apply { value = null }


    companion object{
        private var mInstance: CountriesRepository? = null

        fun getInstance(): CountriesRepository?{
            if(mInstance == null){
                synchronized(this){
                    mInstance = CountriesRepository()
                }
            }

            return mInstance
        }


    }

    fun getWeather(callback: NetworkResponseCallback, cityName : String, stateCode: String, countryCode : String ):MutableLiveData<WeatherInfo> {
        mCallback = callback
        if(!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(countryCode)  && !TextUtils.isEmpty(stateCode)  ) {
            mCountryCall = RestClient.getInstance().getApiService()
                .getWeatherByFullName(cityName, stateCode, countryCode, ConstantVal.API_KEY)
        } else if(!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(countryCode)){
            mCountryCall = RestClient.getInstance().getApiService().getWeatherByCityAndCountrycode(cityName,countryCode, ConstantVal.API_KEY)
        }else{
            mCountryCall = RestClient.getInstance().getApiService().getWeatherByCity(cityName, ConstantVal.API_KEY)
        }
        mCountryCall.enqueue(object : Callback<WeatherInfo> {

            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                Logutil.d("onResponse onNetworkSuccess "+ Gson().toJson(response))
                mCountryList.value = response.body()
                response.body()?.let { mCallback.onNetworkSuccess(it) }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                // mCountryList.value = call
                Logutil.d("onResponse onFailure")
                mCallback.onNetworkFailure("Response failure")
            }

        })
        return mCountryList
    }


}
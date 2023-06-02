package com.example.weatherapp.ui.util

import android.util.Log

class Logutil {



    companion object{

        private const val TAG :String = "WeatherApp"
        private const val isLogging : Boolean = true

        fun d( message: String){
            if(isLogging)
            Log.d(TAG, message)
        }
    }
}
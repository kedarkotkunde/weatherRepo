package com.example.weatherapp.ui.util

import android.app.Activity
import android.content.Context

class ConstantVal {

    companion object{
        final const val API_KEY ="8464e632342397804902c893305c789c"
        final const val PREF_KEY ="CITYNAME"

        fun saveCityDetails(activity: Activity , cityName: String){
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(com.example.weatherapp.ui.util.ConstantVal.PREF_KEY, cityName)
                apply()
            }
            Logutil.d("Details save as "+cityName)
        }

        fun getCityName(activity: Activity): String?{
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            return  sharedPref!!.getString(ConstantVal.PREF_KEY, "")

        }

    }
}
package com.example.weatherapp.ui


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFirstBinding
import com.example.weatherapp.ui.models.WeatherInfo
import com.example.weatherapp.ui.util.ConstantVal
import com.example.weatherapp.ui.util.Logutil
import com.example.weatherapp.ui.util.NetworkHelper
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var mViewModel: WeatherViewModel
    lateinit var position :LatLng
    lateinit var cityName :String
    private val binding get() = _binding!!
    private var mMap: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.mainMap) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        mViewModel.test.observe(viewLifecycleOwner, Observer { kt->
            context?.let {
                if(kt.sys?.country.toString().equals("US")) {
                    updateUI(kt, it)
                }else{
                    Toast.makeText(activity, "Entry US city only", Toast.LENGTH_LONG).show()
                }
            }
        })
        activity?.let {
            val cityName =ConstantVal.getCityName(it)
            _binding?.textViewTitle?.text= cityName
            Logutil.d("Last time saved "+ cityName)
            val isConnected =context?.let { it1 -> NetworkHelper.isNetworkAvailable(it1) }
            if( isConnected== false){
                Toast.makeText(activity, "Please connect to internet", Toast.LENGTH_LONG).show()
            }
            if(!TextUtils.isEmpty(cityName) && isConnected== true){
                getWeatherUpdate(cityName)
            }
        }
        _binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getWeatherUpdate(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getWeatherUpdate(query: String?){
        if(!TextUtils.isEmpty(query.toString())){
            cityName =  query.toString()
            val arrayList = query.toString().split(",")
            when(arrayList.size){
                1->mViewModel.fetchWeatherFromServer(arrayList.get(0), "", "")
                2-> mViewModel.fetchWeatherFromServer(arrayList.get(0), arrayList.get(1), "")
                3->mViewModel.fetchWeatherFromServer(arrayList.get(0), arrayList.get(1), arrayList.get(2))
            }

        }else{
            Toast.makeText(activity, "City name field should not be empty", Toast.LENGTH_LONG).show()
        }
    }

    fun updateUI(weatherInfo: WeatherInfo, context: Context){
        Logutil.d("Code is "+weatherInfo.cod)
        _binding?.textViewTitle?.text= cityName
        _binding!!.textViewTemp.text = "Temp ${weatherInfo.main!!.temp.toString()}"
        _binding!!.textViewFeelsLike.text = "Feels like  ${weatherInfo.main!!.feelsLike.toString()}"
        _binding!!.textViewHumidity.text = "Humidity ${weatherInfo.main!!.humidity.toString()}"
        _binding!!.textViewPressure.text = "Pressure  ${weatherInfo.main!!.pressure.toString()}"

        _binding!!.textViewSpeed.text = "Wind Speed  ${weatherInfo.wind!!.speed.toString()}"
        _binding!!.textViewDeg.text = "Degree  ${weatherInfo.wind!!.deg.toString()}"
        _binding!!.textViewGust.text = "Gust  ${weatherInfo.wind!!.gust.toString()}"

        position = LatLng(weatherInfo.coord!!.lat!!, weatherInfo.coord!!.lon!!)
        activity?.let { ConstantVal.saveCityDetails(it, cityName) }
        updateMap()
        val arrayList = weatherInfo.weather
        var icon : String ="";
        for (item in arrayList){
            Logutil.d("${item.icon}")
            icon = item.icon.toString()
            break
        }
        val urlImage ="https://openweathermap.org/img/wn/$icon@2x.png"
        Logutil.d("Urlimage $urlImage")
            Glide.with(context)
                .load(urlImage)
                .fitCenter()
                .into(_binding!!.imageView);

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateMap(){
        mMap!!.addMarker( MarkerOptions().position(position))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 16f)
        mMap!!.animateCamera(cameraUpdate, 2000, null)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
    }


}
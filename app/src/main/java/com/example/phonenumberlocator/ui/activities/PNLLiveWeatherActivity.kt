package com.example.phonenumberlocator.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlliveWeatherBinding
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.pnlDialog.PNLResumeLoadingDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kwabenaberko.openweathermaplib.constant.Languages
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class PNLLiveWeatherActivity : PNLBaseClass<ActivityPnlliveWeatherBinding>(){
    lateinit var helper: OpenWeatherMapHelper;
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var locationManager: LocationManager? = null
    private lateinit var currentLocation: Location
    var city = ""
    var country = ""
    private var dialog: PNLResumeLoadingDialog?=null

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  dialog = PNLResumeLoadingDialog(this)
        if (isTimeDifferenceGreaterThan30Seconds()) {
            if (isNetworkAvailable()) {
                dialog?.show()
                showHighMainAdmobInterstitial({
                    previousAdClosedTime = System.currentTimeMillis()
                }, {
                    showLowMainAdmobInterstitial({
                        previousAdClosedTime = System.currentTimeMillis()
                    }, { dialog?.dismiss() }, {
                        interstitialCounter =0
                        delayAdShown = true
                        Handler().postDelayed({
                            dialog?.dismiss()
                        }, 1000)
                    })
                }, {
                    interstitialCounter =0
                    delayAdShown = true
                    Handler().postDelayed({
                        dialog?.dismiss()
                    }, 1000)
                })

            }
        }
        loadAd()*/
        initViews()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        iniit()
    }

    private fun iniit() {
//        sunTimings()
        editCurrTime()
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Toast.makeText(this, resources.getString(R.string.enable_location), Toast.LENGTH_SHORT).show()
        } else {
            getLastLocation()
        }
        binding.backArrowW.setOnClickListener {
            finish()
        }


    }


    override fun onResume() {
        super.onResume()
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLastLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun initViews() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        helper = OpenWeatherMapHelper("6d08bf86106efb9db72c176a21d23ebf")

//        val svg = SVG.getFromResource(this,R.raw.wind_power)
//        val drawable: Drawable = PictureDrawable(svg.renderToPicture())
//        binding.ivWind.setImageDrawable(drawable)
//
//        val svg1 = SVG.getFromResource(this, R.raw.pressure)
//        val drawable1: Drawable = PictureDrawable(svg1.renderToPicture())
//        binding.ivPressure.setImageDrawable(drawable1)

    }

    private fun editCurrTime() {
        val time = System.currentTimeMillis()
        //format time
        val timeFormat = DateFormat.format("EEE, MMM d, yyyy", time) as String
        Log.e("resultdateFormattedDate", "timeOfTheWeek=>$timeFormat")
//        binding.txtTimeWeather.text = timeFormat
    }


    private fun getCurrentData(cityName: String) {
        helper.setUnits(Units.METRIC)
        helper.setLanguage(Languages.ENGLISH)

        helper.getCurrentWeatherByCityName(
            cityName.trim(),
            object : CurrentWeatherCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(currentWeather: CurrentWeather) {
                    val description = currentWeather.weather[0].description.toString()
//                    binding.txtLocWeather.text = getCountryName(currentWeather.coord.lat, currentWeather.coord.lon)
                    binding.txtHumidity.text = currentWeather.main.humidity.toString()
                    binding.txtWind.text = currentWeather.wind.speed.toString() + "m/s"
                    binding.txtPressure.text = currentWeather.main.pressure.toString() + "hPa"
                    binding.txtCloudCover.text = currentWeather.clouds.all.toString() + "%"
                    binding.txtVisibility.text = currentWeather.visibility.toString() + "m"
                    binding.txtFeelslike.text = currentWeather.main.feelsLike.roundToInt().toString()  + "°C"
                    binding.txtTempWeather.text = currentWeather.main.tempMax.roundToInt().toString() + "°C"
                    binding.txtTempState.text = description

                    when (description) {
                        "clear sky" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_clearday)
                        }
                        "few clouds" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_few_clouds)
                        }
                        "scattered clouds" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_scattered)
                        }
                        "broken clouds" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_clearday)
                        }
                        "shower rain" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_shower_rain)
                        }
                        "rain" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_rain)
                        }
                        "thunderstorm" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_thunder_storm)
                        }
                        "snow" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_snow)
                        }
                        "mist" -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_mist)
                        }
                        else -> {
                            binding.imgWeather.setImageResource(R.drawable.ic_default)
                        }
                    }


                    // Picasso.with(context).load(iconUrl).into(yourImageView)

                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("WeatherActivity", throwable.message.toString())
                }
            })
    }

    //current loc
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        @SuppressLint("LogNotTimber")
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            if (mLastLocation != null) {

                Log.e("mLocationCallback", "mLocationCallback==>" + mLastLocation.latitude + ": " + mLastLocation.latitude)
            }
            val lat = mLastLocation?.latitude
            val lng = mLastLocation?.longitude
            if (lat != null) {
                if (lng != null) {
                    getAddress(lat, lng)
                }
            }

        }
    }

    private fun getLastLocation() {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        currentLocation = location
                        val lat = location.latitude
                        val lng = location.longitude
                        getCurrentData(getAddress(lat, lng))
                    }
                }


            } else {
                Toast.makeText(this, resources.getString(R.string.turn_on_location), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    fun getAddress(lat: Double, lng: Double):String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if(addresses!=null && addresses.size>0){
                val obj = addresses[0]

                if(obj!=null){
                    city = obj.locality.toString()
                    country = obj.countryName.toString()

                    Log.e("country", "=>$country")
                    Log.e("city", "=>$city")
//                    binding.txtLocWeather.text = city+","+country
                }else{
//                    binding.txtLocWeather.text = resources.getString(R.string.cannot_find_data)
                }



            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        return city
    }
    fun getCountryName(lat: Double, lng: Double):String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if(addresses!=null && addresses.size>0){
                val obj = addresses[0]
                if(obj.locality==null){
//                    city=txtLocationCity.text.toString()
                }else{
                    city = obj.locality
                }

                if(obj.countryName==null){
                    country = ""
                }else{
                    country = obj.countryName
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        return "$city,$country"
    }

    override fun getViewBinding(): ActivityPnlliveWeatherBinding {
        return ActivityPnlliveWeatherBinding.inflate(layoutInflater)
    }

    /*  private fun sunTimings(){
          val srt = calculateSunriseTime()
          val sunriseFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
          binding.sunriseTime.text = sunriseFormatter.format(srt)

          val sst = calculateSunsetTime()
          val sunsetFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
          binding.sunsetTime.text = sunsetFormatter.format(sst)
      }
      private fun calculateSunriseTime(): Date {
          val calendar = Calendar.getInstance()
          calendar.set(Calendar.HOUR_OF_DAY, 7)
          calendar.set(Calendar.MINUTE, 6)
          return calendar.time
      }
      private fun calculateSunsetTime(): Date {
          val calendar = Calendar.getInstance()
          calendar.set(Calendar.HOUR_OF_DAY, 18)
          calendar.set(Calendar.MINUTE, 10)
          return calendar.time
      }*/

   /* private fun loadAd() {
        if (isNetworkAvailable()) {
            binding.ads.beVisible()
            TrackLocationAppClass.instance.nativeAdOther.observe(this){
                showLoadedNativeAd(this,binding.ads, R.layout.layout_admob_native_ad,it)
            }
        } else {
            binding.ads.beGone()
        }
    }*/

}

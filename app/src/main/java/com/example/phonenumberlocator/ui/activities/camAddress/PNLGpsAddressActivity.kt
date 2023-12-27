package com.example.phonenumberlocator.ui.activities.camAddress

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.showBannerAdmob
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnlcamAddressBinding
import com.example.phonenumberlocator.pnlExtensionFun.getAddressFromLatLong
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.ui.pnlDialog.PNLLoadingDialog
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.kwabenaberko.openweathermaplib.constant.Languages
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.ctx
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class PNLGpsAddressActivity: AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityPnlcamAddressBinding

    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 2

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection
    private var address: String? = null
    private var loadingDialog: PNLLoadingDialog? = null

    var croppedBitmap: Bitmap? = null
    lateinit var helper: OpenWeatherMapHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlcamAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleAds()

        helper = OpenWeatherMapHelper("6d08bf86106efb9db72c176a21d23ebf")
        val imagePath = intent.getStringExtra("imagePath")
        if (imagePath != null) {
            // Load the image using the imagePath
            val imageFile = File(imagePath)
            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            binding.clickImage.setImageBitmap(imageBitmap)
        }
        // Set click listener on take picture button
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocation()
        }

        /* // Get a reference to the view from which to take the screenshot
         binding.ivSave.setOnClickListener {
             takeScreenshot(this)
             startActivity(Intent(this@CamAddressActivity, SavedImageConfirmationActivity::class.java)
                 .putExtra("ss",croppedBitmap))
             finish()
 //            toast(R.string.saved)
 //            binding.cardSave.beGone()
 //            binding.cardShare.beVisible()
         }*/
        binding.ivSave.setOnClickListener {
                val filePath = takeScreenshot(this)
                if (filePath != null) {
                    startActivity(
                        Intent(this, PNLSavedImageConfirmationActivity::class.java)
                        .putExtra("imagePath", filePath))
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save the screenshot", Toast.LENGTH_SHORT).show()
                }

        }
        /*  binding.cardShare.setOnClickListener {
              isAppOpenEnable=true
              share(croppedBitmap!!)
          }*/
        binding.backArrow.setOnClickListener { onBackPressed() }
    }


    private fun getLocation() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            Log.d("TAG", "getLocation: here1")
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

//tariq code for current location
                // Get a reference to the FusedLocationProviderClient
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

// Request the last known location of the user
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(
                        this
                    ) { location -> // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            Log.d("Location12", "Latitude: $latitude Longitude: $longitude")
                            binding.etLatitude.text = latitude.toString()
                            binding.etLongitude.text = longitude.toString()
                            Log.d("TAG", "Latitude: $latitude, Longitude: $longitude")
                            getCurrentData(latitude,longitude)

                            checkInternetConnection.observe(this) {
                                if (it) {
                                    address = getAddressFromLatLong(
                                        location.latitude,
                                        location.longitude
                                    )
                                    if (address != "Please Check internet Connection") {
                                        loadingDialog?.hideDialog()
//                                binding.cardView.beGone()
                                        binding.currentAddress.text = address
                                    } else {
//                                binding.cardView.beGone()
                                        toast(R.string.check_internet_connection)
                                        loadingDialog?.hideDialog()
                                    }
                                } else {
//                            binding.cardView.beGone()
                                    toast(R.string.check_internet_connection)
                                    loadingDialog?.hideDialog()
                                }
                            }

                        }
                    }
            } else {
                // GPS is not enabled, prompt the user to enable it
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                // Permission denied, show an error message
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d("TAG", "Latitude: $latitude, Longitude: $longitude")
    }

    // Define a function to take a screenshot and save it to the gallery
    /* private fun takeScreenshot(context: Context) {
         // Get the root view of the activity
         val view = binding.clImg

         // Create a bitmap of the view
         croppedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
         val canvas = Canvas(croppedBitmap!!)
         view.draw(canvas)

         // Save the bitmap to the gallery
         val contentValues = ContentValues().apply {
             put(
                 MediaStore.Images.Media.DISPLAY_NAME, "Screenshot_${System.currentTimeMillis()}.png"
             )
             put(MediaStore.Images.Media.MIME_TYPE, "image/png")
             put(MediaStore.Images.Media.WIDTH, croppedBitmap?.width)
             put(MediaStore.Images.Media.HEIGHT, croppedBitmap?.height)
         }
         val uri = context.contentResolver.insert(
             MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
         )
         if (uri != null) {
             context.contentResolver.openOutputStream(uri).use { outputStream ->
                 croppedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
             }
         }

     }*/
    private fun takeScreenshot(context: Context): String? {
        val view = binding.clImg
        croppedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(croppedBitmap!!)
        view.draw(canvas)

        val fileName = "Location_${System.currentTimeMillis()}.png"
        val filePath = context.getExternalFilesDir(null)?.absolutePath + File.separator + fileName

        try {
            val fileOutputStream = FileOutputStream(filePath)
            croppedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return filePath
    }

    private fun share(bitmap: Bitmap) {
        val pathofBmp = MediaStore.Images.Media.insertImage(
            ctx.contentResolver, bitmap, "title", null
        )
        val uri = Uri.parse(pathofBmp)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Star App")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        ctx.startActivity(Intent.createChooser(shareIntent, "hello hello"))
    }

    override fun onResume() {
        super.onResume()
        showBannerAdmob(binding.flBanner,this,getString(R.string.ad_mob_banner_id))
    }
    private fun convertMetersPerSecondToKilometersPerHour(speedInMetersPerSecond: Double): Double {
        return speedInMetersPerSecond * 3.6
    }
    private fun getCurrentData(latitude: Double, longitude: Double) {
        helper.setUnits(Units.METRIC)
        helper.setLanguage(Languages.ENGLISH)

        helper.getCurrentWeatherByGeoCoordinates(
            latitude,
            longitude,
            object : CurrentWeatherCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(currentWeather: CurrentWeather) {
                    val windSpeedMetersPerSecond = currentWeather.wind.speed
                    val windSpeedKilometersPerHour = convertMetersPerSecondToKilometersPerHour(windSpeedMetersPerSecond)
                    binding.tvWind.text = "${windSpeedKilometersPerHour.roundToInt()} km/h"
                    binding.tvTemp.text = currentWeather.main.tempMax.roundToInt().toString() + "°C"
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("WeatherActivity", throwable.message.toString())
                }
            })
    }

    private fun handleAds(){
        if (isNetworkAvailable()){
            showSimpleInterstitial()

        }
    }
}


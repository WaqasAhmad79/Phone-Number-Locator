package com.example.phonenumberlocator.ui.activities.gpsTracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canRequestAd
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLarge
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.isAppOpenEnable
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityGpsLocationBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.getAddressFromLatLong
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.ui.pnlDialog.PNLLoadingDialog
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GpsLocationActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityGpsLocationBinding

    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 2

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection
    private var address: String? = null
    private var loadingDialog: PNLLoadingDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (RemoteConfigClass.inter_gps_location_activity
            && isNetworkAvailable()
            && canRequestAd
        ) {
            showSimpleInterstitialAdWithTimeAndCounter()
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
        binding.backArrow.setOnClickListener { onBackPressed() }

        binding.getLocation.setOnClickListener {
            val intent = Intent(this, ShowOnMapActivity::class.java)
            startActivity(intent)
        }


        showAd()
    }


    private fun getLocation() {
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
                            binding.tvLati.text = latitude.toString()
                            binding.tvLong.text = longitude.toString()
                            Log.d("TAG", "Latitude: $latitude, Longitude: $longitude")
                            shareGetAddressFromLatLong(latitude, longitude)

                            checkInternetConnection.observe(this) {
                                if (it) {
                                    address = getAddressFromLatLong(
                                        location.latitude,
                                        location.longitude
                                    )
                                    if (address != "Please Check internet Connection") {
                                        loadingDialog?.hideDialog()
                                        binding.tvAddress.text = address
                                    } else {
                                        toast(R.string.check_internet_connection)
                                        loadingDialog?.hideDialog()
                                    }
                                } else {
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

    override fun onResume() {
        isAppOpenEnable = false

        super.onResume()
    }

    private fun shareGetAddressFromLatLong(
        latitude: Double,
        longitude: Double,
        callBack: (() -> Unit)? = null
    ): String {
        val addresses: List<Address>?
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: Exception) {
            callBack?.invoke()
            return "Please Check internet Connection"
        }

        val addressText: String
        val cityText: String
        val countryText: String
        val postalText: String

        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            addressText = address.getAddressLine(0) ?: ""
            cityText = address.locality ?: ""
            countryText = address.countryName ?: ""
            postalText = (address.postalCode ?: "")
//            loadingDialog?.hideDialog()
        } else {
            addressText = getString(R.string.no_address_found)
            cityText = ""
            countryText = ""
            postalText = ""
        }

        binding.tvAddress.text = addressText
        binding.tvCity.text = cityText
        binding.tvCountry.text = countryText
        binding.tvPostal.text = postalText


        callBack?.invoke()
        return addressText
    }

    private fun showAd() {
        if (RemoteConfigClass.native_gps_location_activity) {

            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                nativeAdLarge.observe(this) {
                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.layout_admob_native_ad,
                        it
                    )
                }
            }

        } else {
            binding.ads.beGone()
        }
    }

}



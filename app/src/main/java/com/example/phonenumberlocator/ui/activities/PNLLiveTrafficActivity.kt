package com.example.phonenumberlocator.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlliveTrafficBinding
import com.example.phonenumberlocator.pnlExtensionFun.beInvisible
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlInterfaces.MyLocationCallback
import com.example.phonenumberlocator.pnlUtil.PNLSphericalUnitsUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PNLLiveTrafficActivity : PNLBaseClass<ActivityPnlliveTrafficBinding>(), OnMapReadyCallback {
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var googleMap: GoogleMap? = null
    var locationManager: LocationManager? = null

    private var zoom = 16
    private var myLocation: LatLng? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var lastMyLocationCallback: MyLocationCallback? = null
    private  val REQUEST_LOCATION_PERMISSION = 0
    private var is3DViewEnabled = false



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.liveTrafficMap) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000, 10f, locationListenerGPS
            )
        }
        when {
            !locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
            }
            else -> {
                getLastLocation()
            }
        }
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.cardPlus.setOnClickListener {
            if (zoom < 19) zoom++
            myLocation?.let { it1 -> moveMapCamera(it1, zoom) }
        }
        binding.cardMinus.setOnClickListener {
            if (zoom > 3) zoom--
            myLocation?.let { it1 -> moveMapCamera(it1, zoom) }
        }
        binding.cardMyLocation.setOnClickListener {
            setCurrentLocation()
        }

        binding.cardMap3d.setOnClickListener {
            it.beInvisible()
            binding.cardMap2d.beVisible()
            onSwitch3DButtonClicked(it)
        }
        binding.cardMap2d.setOnClickListener {
            it.beInvisible()
            binding.cardMap3d.beVisible()
            onSwitch3DButtonClicked(it)
        }

    }
    private fun onSwitch3DButtonClicked(view: View) {
        // Check if the map is ready
        googleMap?.let {
            // Toggle 3D view on/off
            is3DViewEnabled = !is3DViewEnabled

            // Get the current camera position
            val cameraPosition = it.cameraPosition

            // Create a new camera position with the updated tilt (for 3D effect)
            val newCameraPosition = CameraPosition.Builder(cameraPosition)
                .tilt(if (is3DViewEnabled) 60.0f else 0.0f)
                .build()

            // Animate the camera to the new position
            it.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setCurrentLocation() {
        getCurrentLocation(object : MyLocationCallback {
            @SuppressLint("SuspiciousIndentation")
            override fun gotAllLocation(location: Location?) {
                if (location != null) {
                    myLocation = LatLng(location.latitude, location.longitude)
                    // Add a marker at the user's location
                    googleMap?.addMarker(
                        MarkerOptions().position(myLocation!!).title(resources.getString(
                            R.string.current_location)))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation!!, 15f))
                    val distance: Double = PNLSphericalUnitsUtil.computeDistanceBetween(
                        myLocation!!,
                        googleMap!!.cameraPosition.target
                    )
                    if (distance < 0.5) {
                        Toast.makeText(
                            this@PNLLiveTrafficActivity, R.string.current_location,
                            Toast.LENGTH_SHORT
                        ).show()
//                        addPoint(myLocation!!)
                    } else {
                        moveMapCamera(myLocation!!)

                    }
                }
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCurrentLocation(callback: MyLocationCallback?) {
        if (hasLocationPermission()) {
            if (callback == null) return
            mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                        @SuppressLint("MissingPermission") val l = LocationServices.FusedLocationApi
                            .getLastLocation(mGoogleApiClient!!)
                        mGoogleApiClient?.disconnect()
                        callback.gotAllLocation(l)
                    }

                    override fun onConnectionSuspended(cause: Int) {
                    }
                }).build()
            mGoogleApiClient!!.connect()
        } else {
            lastMyLocationCallback = callback
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),REQUEST_LOCATION_PERMISSION
            )
        }
    }
    private fun hasLocationPermission(): Boolean {
        return PermissionChecker
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED && PermissionChecker
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED
    }

    fun moveMapCamera(pos: LatLng) {
        moveMapCamera(pos, zoom)
    }
    @SuppressLint("LogNotTimber")
    private fun moveMapCamera(pos: LatLng, zoom: Int) {
        Log.d(TAG, "moveCamera zoom: $zoom")
        Log.d(TAG, "moveCamera zoom.toFloat: ${zoom.toFloat()}")
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                pos,
                zoom.toFloat()
            )
        )
        //map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom.toFloat()))
    }


    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
            val latitude = location.latitude
            val longitude = location.longitude
            val curLatLng = LatLng(latitude, longitude)
            val currtMarker = MarkerOptions().position(curLatLng)
            val marker = googleMap?.addMarker(currtMarker);
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15.0f));

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.e("called", " onMapReady")
        this.googleMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        googleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                val curLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                val currtMarker = MarkerOptions().position(curLatLng)
                var marker = googleMap?.addMarker(currtMarker)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15.0f));
                googleMap?.isTrafficEnabled = true

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        setCurrentLocation()
//        setLocale()
//        init()
        super.onResume()

        when {
            !locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
            }
            else -> {
                getLastLocation()
            }
        }
    }

    override fun onDestroy() {

        super.onDestroy()
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
                        val curLatLng = LatLng(lat, lng)
                        val currtMarker = MarkerOptions().position(curLatLng)
                        googleMap?.addMarker(currtMarker)
                        googleMap?.isTrafficEnabled = true
                        googleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                curLatLng,
                                15.0f
                            )
                        );
                    }
                }


            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            Toast.makeText(applicationContext, "==>request perm", Toast.LENGTH_LONG).show()
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

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
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation!!
            Log.e("mLocationCallback", "mLocationCallback==>" + mLastLocation.latitude + ": " + mLastLocation.latitude)
            val lat = mLastLocation.latitude
            val lng = mLastLocation.longitude
            val curLatLng = LatLng(lat, lng)
            val currtMarker = MarkerOptions().position(curLatLng)
            val marker = googleMap?.addMarker(currtMarker)
            googleMap?.isTrafficEnabled = true
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15.0f));

        }
    }

    override fun getViewBinding(): ActivityPnlliveTrafficBinding {
        return ActivityPnlliveTrafficBinding.inflate(layoutInflater)
    }

}
package com.example.phonenumberlocator.ui.activities.gpsTracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canRequestAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.isAppOpenEnable
import com.example.phonenumberlocator.admob_ads.showBannerAdmob
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityPnlfindAddressBinding
import com.example.phonenumberlocator.pnlExtensionFun.*
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PNLFindAddressActivity : PNLBaseClass<ActivityPnlfindAddressBinding>() {

    override fun getViewBinding(): ActivityPnlfindAddressBinding {
        return ActivityPnlfindAddressBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection


    private var counter = 1
    private var zoom = 16
    private var smf: SupportMapFragment? = null
    private var client: FusedLocationProviderClient? = null
    private var latLng: LatLng? = null
    private var latLngCurrent: LatLng? = null
    private var address: String? = null
    private var is3DViewEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleAds()

        gpsStatusCheck() {
            if (it) {
                initView()

            }
        }
        clickListeners()

        if (RemoteConfigClass.banner_pnl_find_address_activity && PhoneNumberLocator.canRequestAd) {
            showBannerAdmob(binding.flBanner, this, getString(R.string.ad_mob_banner_id))
        } else {
            binding.flBanner.beGone()
        }

    }

    fun handleAds() {

        if (RemoteConfigClass.inter_pnl_find_address_activity
            && isNetworkAvailable()
            && canRequestAd
        ) {
            showSimpleInterstitialAdWithTimeAndCounter()
        }

    }

    private fun initView() {
        smf = supportFragmentManager.findFragmentById(R.id.coordinatorMap) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        Dexter.withContext(applicationContext)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    getMyLocation()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }

    private fun clickListeners() {

        binding.etLatitude.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }
        binding.etLongitude.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
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


        binding.cardPlus.setOnClickListener {
            if (zoom < 19) zoom++
            latLng?.let { it1 ->
                moveMapCamera(it1, zoom)
            }
        }

        binding.cardMinus.setOnClickListener {
            if (zoom > 3) zoom--
            latLng?.let { it1 -> moveMapCamera(it1, zoom) }
        }

        binding.getLocation.setOnClickListener {
            performSearch()
        }
        binding.cardMyLocation.setOnClickListener {
            getMyLocation()
        }
        binding.backArrow.setOnClickListener { onBackPressed() }

        binding.resultAddress.setOnClickListener { }

        binding.cardCopy.setOnClickListener {
            if (binding.tvAddress.text.toString().trim().isNotEmpty()) {
                copyText(binding.tvAddress.text.toString())
                toast(R.string.address_copied)
            }
        }

        binding.cardShare.setOnClickListener {
            isAppOpenEnable = true
            latLng?.let {
                address?.let { add -> shareCurrentLocation(it, add) }
            }
        }

        /* binding.cardNavigation.setOnClickListener {
 //            isInterstitialAdOnScreen=true
             latLng?.let { destination ->
                 latLngCurrent?.let { currentLoc ->
                     if (destination != currentLoc) {
                         navigation(currentLoc, destination)
                     } else (
                             toast(R.string.already_destination)
                             )
                 }
             }
         }*/


    }

    private fun onSwitch3DButtonClicked(view: View) {
        // Check if the map is ready
        smf?.getMapAsync { googleMap ->
            // Toggle 3D view on/off
            is3DViewEnabled = !is3DViewEnabled

            // Get the current camera position
            val cameraPosition = googleMap.cameraPosition

            // Create a new camera position with the updated tilt (for 3D effect)
            val newCameraPosition = CameraPosition.Builder(cameraPosition)
                .tilt(if (is3DViewEnabled) 60.0f else 0.0f)
                .build()

            // Animate the camera to the new position
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
        }
    }

    private fun moveMapCamera(pos: LatLng, zoom: Int) {
        Log.d(TAG, "moveCamera zoom: $zoom")
        Log.d(TAG, "moveCamera zoom.toFloat: ${zoom.toFloat()}")
        smf?.getMapAsync {
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pos, zoom.toFloat()
                )
            )
        }
        //map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom.toFloat()))
    }

    private fun performSearch() {
        binding.etLatitude.hideKeyboard(this)
        val lat = binding.etLatitude.text.toString().trim()
        val lang = binding.etLongitude.text.toString().trim()
        if (lat.isNotEmpty() && lang.isNotEmpty()) {
            val latitude = lat.toDouble()
            val longitude = lang.toDouble()
            latLng = LatLng(latitude, longitude)
            showOnMap()


        }
    }

    fun getMyLocation() {
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
        val task = client?.lastLocation
        task?.addOnSuccessListener { location ->
            latLng = location?.latitude?.let { location.longitude.let { it1 -> LatLng(it, it1) } }
            latLngCurrent = latLng
            binding.etLatitude.setText(location?.latitude.toString())
            binding.etLongitude.setText(location?.longitude.toString())
            showOnMap()
        }
    }

    private fun showOnMap() {
        /**for Marker move on putting the values of  latitude and longitude **/
        /* smf?.getMapAsync { googleMap ->
             val markerOptions =
                 latLng?.let {
                     MarkerOptions().position(it)
                         .title(resources.getString(R.string.current_location))
                 }
             markerOptions?.let {
                 googleMap.addMarker(markerOptions)
             }
             latLng?.let {
                 googleMap.animateCamera(
                     CameraUpdateFactory.newLatLngZoom(
                         it,
                         fl
                     )
                 )
             }
         }*/
        /**for Marker move on the click of map and give latitude and longitude **/
        smf?.getMapAsync { googleMap ->
            googleMap.clear()
            val markerOptions =
                latLng?.let {
                    MarkerOptions().position(it)
                        .title(resources.getString(R.string.current_location))
                }
            if (markerOptions != null) {
                googleMap.addMarker(markerOptions)
            }
            latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
                ?.let { googleMap.animateCamera(it) }
            googleMap.setOnMapClickListener {
                latLng = it
                binding.etLatitude.setText(latLng?.latitude.toString())
                binding.etLongitude.setText(latLng?.longitude.toString())
                showOnMap()
            }
        }
        checkInternetConnection.observe(this) {
            if (it) {
                address = latLng?.latitude?.let { lat ->
                    latLng?.longitude?.let { long ->
                        getAddressFromLatLong(
                            lat,
                            long
                        )
                    }
                }
                if (address != "Please Check internet Connection") {
//                    loadingDialog?.hideDialog()
                    binding.getLocationResult.beVisible()
                    binding.tvAddress.text = address
                } else {
                    binding.getLocationResult.beGone()
                    toast(R.string.check_internet_connection)
//                    loadingDialog?.hideDialog()
                }
            } else {
                binding.getLocationResult.beGone()
                toast(R.string.check_internet_connection)
//                loadingDialog?.hideDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isAppOpenEnable = false
    }

}